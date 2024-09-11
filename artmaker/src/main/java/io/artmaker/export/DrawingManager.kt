/*
 * Copyright 2024 ArtMaker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.artmaker.export

import android.graphics.PointF
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import io.artmaker.actions.DrawEvent
import io.artmaker.actions.UndoRedoAction
import io.artmaker.models.PointsData
import io.artmaker.utils.eraseLines
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.Stack
import kotlin.properties.Delegates

/**
 * The drawing manager will handle all the logic related to drawing including clearing, undo, and redo
 */
internal class DrawingManager {
    private val undoStack = Stack<UndoRedoAction>()
    private val redoStack = Stack<UndoRedoAction>()

    private val _pathList = mutableStateListOf<PointsData>()
    val pathList: SnapshotStateList<PointsData> = _pathList

    private val _undoRedoState = MutableStateFlow(UndoRedoState())
    val undoRedoState: StateFlow<UndoRedoState> = _undoRedoState

    private var strokeWidth by Delegates.notNull<Int>()

    fun onDrawEvent(event: DrawEvent, strokeColor: Int, strokeWidth: Int) {
        when (event) {
            is DrawEvent.AddNewShape -> addNewShape(event.offset, strokeColor, strokeWidth)
            DrawEvent.UndoLastShapePoint -> undoLastShapePoint()
            is DrawEvent.UpdateCurrentShape -> updateCurrentShape(event.offset)
            DrawEvent.Clear -> clear()
            DrawEvent.Redo -> redo()
            DrawEvent.Undo -> undo()
            is DrawEvent.Erase -> erase(event.offset)
        }
    }

    private fun addNewShape(offset: Offset, strokeColor: Int, strokeWidth: Int) {
        this.strokeWidth = strokeWidth
        val data = PointsData(
            points = mutableStateListOf(offset),
            strokeColor = Color(strokeColor),
            strokeWidth = strokeWidth.toFloat(),
        )
        undoStack.push(UndoRedoAction.Draw(data))
        _pathList.add(data)
        redoStack.clear()
        _undoRedoState.update { computeUndoRedoState() }
    }

    private fun updateCurrentShape(offset: Offset) {
        val idx = _pathList.lastIndex
        _pathList[idx].points.add(offset)
    }

    private fun undoLastShapePoint() {
        val idx = _pathList.lastIndex
        _pathList[idx].points.removeLast()
    }

    private fun redo() {
        if (redoStack.isEmpty()) return
        when (val action = redoStack.pop()) {
            is UndoRedoAction.Draw -> {
                undoStack.push(UndoRedoAction.Draw(action.pathData))
                _pathList.add(action.pathData)
            }
            is UndoRedoAction.Erase -> {
                undoStack.push(UndoRedoAction.Erase(_pathList.toList(), action.newState))
                _pathList.clear()
                _pathList.addAll(action.newState)
            }
        }
        _undoRedoState.update { computeUndoRedoState() }
    }

    private fun undo() {
        if (undoStack.isEmpty()) return
        when (val action = undoStack.pop()) {
            is UndoRedoAction.Draw -> {
                redoStack.push(UndoRedoAction.Draw(_pathList.last()))
                _pathList.removeLast()
            }
            is UndoRedoAction.Erase -> {
                redoStack.push(UndoRedoAction.Erase(_pathList.toList(), action.newState))
                _pathList.clear()
                _pathList.addAll(action.oldState)
            }
        }
        _undoRedoState.update { computeUndoRedoState() }
    }

    private fun clear() {
        _pathList.clear()
        redoStack.clear()
        undoStack.clear()
        _undoRedoState.update { computeUndoRedoState() }
    }

    private fun computeUndoRedoState(): UndoRedoState {
        return UndoRedoState(
            canUndo = undoStack.isNotEmpty(),
            canRedo = redoStack.isNotEmpty(),
            canClear = _pathList.isNotEmpty() || undoStack.isNotEmpty() || redoStack.isNotEmpty(),
            canErase = _pathList.isNotEmpty()
        )
    }

    private fun erase(offset: Offset) {
        val erasedPoint = PointF(offset.x, offset.y)
        // Store the old state before erasing
        val oldState = _pathList.toList()
        val newPoints = eraseLines(
            pointsData = _pathList,
            erasedPoints = arrayOf(erasedPoint)
        )
        // Only push to undoStack if there's an actual change
        if (oldState != newPoints) {
            undoStack.push(UndoRedoAction.Erase(oldState, newPoints))
            // Clear the existing points and add the new points
            _pathList.clear()
            _pathList.addAll(newPoints)

            redoStack.clear()
            _undoRedoState.update { computeUndoRedoState() }
        }
    }
}

internal data class UndoRedoState(
    val canUndo: Boolean = false,
    val canRedo: Boolean = false,
    val canClear: Boolean = false,
    val canErase: Boolean = false,
)
