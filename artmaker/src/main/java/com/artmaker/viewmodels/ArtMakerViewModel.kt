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
package com.artmaker.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artmaker.actions.ArtMakerAction
import com.artmaker.actions.DrawEvent
import com.artmaker.models.PointsData
import com.artmaker.sharedpreferences.ArtMakerSharedPreferences
import com.artmaker.sharedpreferences.PreferenceKeys
import com.artmaker.state.ArtMakerUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Stack

internal class ArtMakerViewModel(
    private val preferences: ArtMakerSharedPreferences,
) : ViewModel() {

    private var _artMakerUIState =
        MutableStateFlow(value = ArtMakerUIState(strokeColour = preferences.get(PreferenceKeys.SELECTED_STROKE_COLOUR, 0)))
    val artMakerUIState = _artMakerUIState.asStateFlow()

    /**
     * Stack to keep track of drawing paths.
     * Each path consists of a series of [PointsData].
     */
    internal val drawPath = Stack<PointsData>()

    /**
     * Similar to drawPath, but used to store paths that have been undone,
     * allowing them to be redone later
     */
    private val redoPath = Stack<PointsData>()

    /**
     * MutableStateFlow used internally to trigger UI updates
     * whenever the drawing state changes significantly.
     */
    internal var reviseTick = MutableStateFlow(0)

    /**
     * Clears the redoPath stack.
     * Typically called before modifying the current drawing path to prevent unwanted redos.
     */
    private fun clearRedoPath() {
        redoPath.clear()
    }

    fun onAction(action: ArtMakerAction) {
        when (action) {
            ArtMakerAction.ExportArt -> exportArt()
            ArtMakerAction.Redo -> redoArt()
            ArtMakerAction.Undo -> undoArt()
            ArtMakerAction.Clear -> clearArt()
            ArtMakerAction.UpdateBackground -> updateBackgroundColour()
            is ArtMakerAction.SelectStrokeColour -> updateStrokeColor(colour = action.color)
            ArtMakerAction.SelectStrokeWidth -> selectStrokeWidth()
        }
    }

    fun onDrawEvent(event: DrawEvent) {
        when (event) {
            is DrawEvent.AddNewShape -> addNewShape(event.offset)
            DrawEvent.UndoLastShapePoint -> undoLastShapePoint()
            is DrawEvent.UpdateCurrentShape -> updateCurrentShape(event.offset)
        }
    }

    private fun addNewShape(offset: Offset) {
        val data = PointsData(points = mutableStateListOf(offset), strokeColor = Color(artMakerUIState.value.strokeColour))
        drawPath.add(data)
    }

    /**
     * Updates the current shape by adding a new point at the given offset.
     * Also clears the redoPath to maintain state consistency.
     */
    private fun updateCurrentShape(offset: Offset) {
        clearRedoPath()
        val idy = drawPath.lastIndex
        drawPath[idy].points.add(offset)
    }

    /**
     * Removes the last point from the current shape, effectively undoing the last action.
     * Also clears the redoPath to maintain state consistency.
     */
    private fun undoLastShapePoint() {
        clearRedoPath()
        val idx = drawPath.lastIndex
        drawPath[idx].points.removeLast()
    }

    private fun exportArt() {}

    /** Executes redo the drawn points if possible. */
    private fun redoArt() {
        if (redoPath.isNotEmpty()) {
            drawPath.push(redoPath.pop())
            reviseTick.value++
        }
    }

    private fun undoArt() {
        if (drawPath.isNotEmpty()) {
            redoPath.push(drawPath.pop())
            reviseTick.value++
        }
    }

    private fun clearArt() {
        drawPath.clear()
        redoPath.clear()
        reviseTick.value++
    }

    private fun updateBackgroundColour() {}

    private fun updateStrokeColor(colour: Color) {
        preferences.set(
            key = PreferenceKeys.SELECTED_STROKE_COLOUR,
            value = colour.toArgb(),
        )
        _artMakerUIState.update {
            it.copy(strokeColour = preferences.get(PreferenceKeys.SELECTED_STROKE_COLOUR, 0))
        }
    }

    private fun selectStrokeWidth() {}

    companion object {
        fun provideFactory(
            context: Context,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ArtMakerViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ArtMakerViewModel(
                        preferences = ArtMakerSharedPreferences(
                            context = context,
                        ),
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel Class")
            }
        }
    }
}
