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

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import io.artmaker.actions.DrawEvent
import io.artmaker.models.PointsData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.Stack

/**
 * The drawing manager will handle all the logic related to drawing including clearing, undo, and redo
 */
internal class DrawingManager {
    private val undoStack = Stack<PointsData>()

    private val _pathList = mutableStateListOf<PointsData>()
    val pathList: SnapshotStateList<PointsData> = _pathList

    private val _undoRedoState = MutableStateFlow(UndoRedoState())
    val undoRedoState: StateFlow<UndoRedoState> = _undoRedoState

    fun onDrawEvent(event: DrawEvent, strokeColor: Int, strokeWidth: Int) {
        when (event) {
            is DrawEvent.AddNewShape -> addNewShape(event.offset, strokeColor, strokeWidth)
            DrawEvent.UndoLastShapePoint -> undoLastShapePoint()
            is DrawEvent.UpdateCurrentShape -> updateCurrentShape(event.offset)
            DrawEvent.Clear -> clear()
            DrawEvent.Redo -> redo()
            DrawEvent.Undo -> undo()
            is DrawEvent.EraseCurrentShape -> eraseCurrentShape(event.offset, strokeWidth = strokeWidth)
        }
    }

    private fun addNewShape(offset: Offset, strokeColor: Int, strokeWidth: Int) {
        val data = PointsData(
            points = mutableStateListOf(offset),
            strokeColor = Color(strokeColor),
            strokeWidth = strokeWidth.toFloat(),
        )
        _pathList.add(data)
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
        if (undoStack.isEmpty()) return
        _pathList.add(undoStack.pop())
        _undoRedoState.update { computeUndoRedoState() }
    }

    private fun undo() {
        if (_pathList.isEmpty()) return
        undoStack.push(_pathList.removeLast())
        _undoRedoState.update { computeUndoRedoState() }
    }

    private fun clear() {
        _pathList.clear()
        undoStack.clear()
        _undoRedoState.update { computeUndoRedoState() }
    }

    private fun computeUndoRedoState(): UndoRedoState {
        return UndoRedoState(
            canUndo = _pathList.isNotEmpty(),
            canRedo = undoStack.isNotEmpty(),
            canClear = _pathList.isNotEmpty(),
        )
    }

    private fun eraseCurrentShape(offset: Offset, strokeWidth: Int) {
        val data = PointsData(
            points = mutableStateListOf(offset),
            strokeColor = Color.White,
            strokeWidth = strokeWidth.toFloat(),
        )
        _pathList.add(data)
        _undoRedoState.update { computeUndoRedoState() }
    }
}

internal data class UndoRedoState(
    val canUndo: Boolean = false,
    val canRedo: Boolean = false,
    val canClear: Boolean = false,
)
