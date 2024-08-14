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

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.artmaker.actions.ArtMakerAction
import com.artmaker.actions.DrawEvent
import com.artmaker.actions.ExportType
import com.artmaker.models.PointsData
import com.artmaker.sharedpreferences.ArtMakerSharedPreferences
import com.artmaker.sharedpreferences.PreferenceKeys
import com.artmaker.state.ArtMakerUIState
import com.artmaker.utils.saveToDisk
import com.artmaker.utils.shareBitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Stack

internal class ArtMakerViewModel(
    private val preferences: ArtMakerSharedPreferences,
    private val applicationContext: Context,
) : ViewModel() {

    private var _artMakerUIState = MutableStateFlow(
        value = ArtMakerUIState(
            strokeColour = preferences.get(
                key = PreferenceKeys.SELECTED_STROKE_COLOUR,
                defaultValue = Color.Red.toArgb(),
            ),
            strokeWidth = preferences.get(
                key = PreferenceKeys.SELECTED_STROKE_WIDTH,
                defaultValue = 5,
            ),
        ),
    )
    val artMakerUIState = _artMakerUIState.asStateFlow()

    private val undoStack = Stack<PointsData>()

    private val _pathList = mutableStateListOf<PointsData>()
    val pathList: SnapshotStateList<PointsData> = _pathList

    private val _shouldTriggerArtExport = MutableStateFlow(false)
    val shouldTriggerArtExport: StateFlow<Boolean> = _shouldTriggerArtExport

    private val _backgroundImage: MutableState<ImageBitmap?> = mutableStateOf(null)
    val backgroundImage: State<ImageBitmap?> = _backgroundImage

    private val exportType = mutableStateOf<ExportType>(ExportType.FinishDrawingImage)

    private val _finishedImage = MutableStateFlow<Bitmap?>(null)
    val finishedImage = _finishedImage.asStateFlow()

    fun onAction(action: ArtMakerAction) {
        when (action) {
            is ArtMakerAction.TriggerArtExport -> triggerArtExport(action.type)
            is ArtMakerAction.ExportArt -> exportArt(action.bitmap)
            ArtMakerAction.Redo -> redo()
            ArtMakerAction.Undo -> undo()
            ArtMakerAction.Clear -> clear()
            ArtMakerAction.UpdateBackground -> updateBackgroundColour()
            is ArtMakerAction.SelectStrokeColour -> updateStrokeColor(colour = action.color)
            is ArtMakerAction.SelectStrokeWidth -> selectStrokeWidth(strokeWidth = action.strokeWidth)
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
        val data = PointsData(
            points = mutableStateListOf(offset),
            strokeColor = Color(artMakerUIState.value.strokeColour),
            strokeWidth = artMakerUIState.value.strokeWidth.toFloat(),
        )
        _pathList.add(data)
    }

    private fun updateCurrentShape(offset: Offset) {
        val idx = _pathList.lastIndex
        _pathList[idx].points.add(offset)
    }

    private fun undoLastShapePoint() {
        val idx = _pathList.lastIndex
        _pathList[idx].points.removeLast()
    }

    private fun triggerArtExport(type: ExportType) {
        exportType.value = type
        _shouldTriggerArtExport.update { true }
    }

    private fun exportArt(bitmap: ImageBitmap) {
        _shouldTriggerArtExport.update { false }
        viewModelScope.launch {
            val bmp = bitmap.asAndroidBitmap()
            if (exportType.value == ExportType.FinishDrawingImage) {
                // At this point, the drawing done and we are going to share it programmatically
                _finishedImage.update { bmp }
                return@launch
            }
            val uri = bmp.saveToDisk(applicationContext)
            shareBitmap(applicationContext, uri)
        }
    }

    private fun redo() {
        if (undoStack.isNotEmpty()) {
            pathList.add(undoStack.pop())
        }
    }

    private fun undo() {
        if (_pathList.isNotEmpty()) {
            undoStack.push(_pathList.removeLast())
        }
    }

    private fun clear() {
        _pathList.clear()
        undoStack.clear()
    }

    private fun updateBackgroundColour() {}

    private fun updateStrokeColor(colour: Color) {
        preferences.set(
            key = PreferenceKeys.SELECTED_STROKE_COLOUR,
            value = colour.toArgb(),
        )
        _artMakerUIState.update {
            it.copy(
                strokeColour = preferences.get(
                    PreferenceKeys.SELECTED_STROKE_COLOUR,
                    defaultValue = 0,
                ),
            )
        }
    }

    fun setImage(bitmap: ImageBitmap?) {
        _backgroundImage.value = bitmap
    }

    private fun selectStrokeWidth(strokeWidth: Int) {
        preferences.set(
            key = PreferenceKeys.SELECTED_STROKE_WIDTH,
            value = strokeWidth,
        )
        _artMakerUIState.update {
            it.copy(
                strokeWidth = preferences.get(
                    PreferenceKeys.SELECTED_STROKE_WIDTH,
                    defaultValue = 5,
                ),
            )
        }
    }

    companion object {
        fun provideFactory(
            application: Application,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ArtMakerViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ArtMakerViewModel(
                        preferences = ArtMakerSharedPreferences(
                            context = application,
                        ),
                        applicationContext = application.applicationContext,
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel Class")
            }
        }
    }
}
