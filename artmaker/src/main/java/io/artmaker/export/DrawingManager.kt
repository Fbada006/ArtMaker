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
    private val undoStack = Stack<PointsData>()

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
            is DrawEvent.Erase -> erase(event.offset, eraserRadius = strokeWidth)
        }
    }

    private fun addNewShape(offset: Offset, strokeColor: Int, strokeWidth: Int) {
        this.strokeWidth = strokeWidth
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
            canClear = _pathList.isNotEmpty() || undoStack.isNotEmpty(),
            canErase = _pathList.isNotEmpty(),
        )
    }

    private fun erase(offset: Offset, eraserRadius: Int) {
        val index = _pathList.first().points.indexOf(offset)

        val pointsData = _pathList[index]
        _pathList.removeAt(index)

        if (index == 0) return // If the deleted point is the first point, return

        if (index == _pathList.size) return // If the deleted point is the last point, return

        val beforePoints = _pathList.subList(0, index)
        val afterPoints = _pathList.subList(index, _pathList.size)

        _pathList.clear()
        _pathList.addAll(beforePoints)
        _pathList.addAll(afterPoints)

//        val erasedPoint = PointF(offset.x, offset.y)
//        val currentLines = _pathList.map { it.points }
//        val (newLines, deletedIndexes) =
//            eraseLines(
//                lines = _pathList,
//                eraseRadius = eraserRadius.toFloat(),
//                erasedPoints = arrayOf(erasedPoint),
//            )
//
//        println("Indexes to delete ----------- ${deletedIndexes.joinToString()}")
//
//        _pathList.clear()
//        _pathList.addAll(newLines.first())
//
//        _undoRedoState.update { computeUndoRedoState() }
    }
}

internal data class UndoRedoState(
    val canUndo: Boolean = false,
    val canRedo: Boolean = false,
    val canClear: Boolean = false,
    val canErase: Boolean = false,
)
