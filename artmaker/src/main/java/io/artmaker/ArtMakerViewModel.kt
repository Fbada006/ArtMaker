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
package io.artmaker

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.artmaker.actions.ArtMakerAction
import io.artmaker.actions.DrawEvent
import io.artmaker.actions.ExportType
import io.artmaker.data.ArtMakerSharedPreferences
import io.artmaker.data.PreferencesManager
import io.artmaker.export.DrawingManager
import io.artmaker.models.PointsData
import io.artmaker.utils.saveToDisk
import io.artmaker.utils.shareBitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ArtMakerViewModel(
    private val preferencesManager: PreferencesManager,
    private val drawingManager: DrawingManager,
    private val applicationContext: Context,
) : ViewModel() {

    private var _uiState = MutableStateFlow(
        value = preferencesManager.loadInitialUIState(),
    )
    val uiState = _uiState.asStateFlow()

    val pathList: SnapshotStateList<PointsData> = drawingManager.pathList

    private val _shouldTriggerArtExport = MutableStateFlow(false)
    val shouldTriggerArtExport: StateFlow<Boolean> = _shouldTriggerArtExport

    private val _backgroundImage: MutableState<ImageBitmap?> = mutableStateOf(null)
    val backgroundImage: State<ImageBitmap?> = _backgroundImage

    private val exportType = mutableStateOf<ExportType>(ExportType.FinishDrawingImage)

    private val _finishedImage = MutableStateFlow<Bitmap?>(null)
    val finishedImage = _finishedImage.asStateFlow()

    init {
        listenToUndoRedoState()
    }

    fun onAction(action: ArtMakerAction) {
        when (action) {
            is ArtMakerAction.TriggerArtExport -> triggerArtExport(action.type)
            is ArtMakerAction.ExportArt -> exportArt(action.bitmap)
            ArtMakerAction.UpdateBackground -> updateBackgroundColour()
            is ArtMakerAction.SelectStrokeColour -> updateStrokeColor(colour = action.color.toArgb())
            is ArtMakerAction.SetStrokeWidth -> selectStrokeWidth(strokeWidth = action.strokeWidth)
            is ArtMakerAction.UpdateSetStylusOnly -> updateStylusSetting(useStylusOnly = action.shouldUseStylusOnly)
            is ArtMakerAction.UpdateSetPressureDetection -> updatePressureSetting(detectPressure = action.shouldDetectPressure)
            is ArtMakerAction.UpdateEnableStylusDialogShow -> updateEnableStylusDialog(canShow = action.canShowEnableStylusDialog)
            is ArtMakerAction.UpdateDisableStylusDialogShow -> updateDisableStylusDialog(canShow = action.canShowDisableStylusDialog)
        }
    }

    fun onDrawEvent(event: DrawEvent) = drawingManager.onDrawEvent(
        event,
        _uiState.value.strokeColour,
        _uiState.value.strokeWidth,
    )

    private fun listenToUndoRedoState() {
        viewModelScope.launch {
            drawingManager.undoRedoState.collectLatest { state ->
                _uiState.update {
                    it.copy(
                        canRedo = state.canRedo,
                        canUndo = state.canUndo,
                        canClear = state.canClear,
                        canErase = state.canErase,
                    )
                }
            }
        }
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

    private fun updateBackgroundColour() {}

    private fun updateStrokeColor(colour: Int) {
        preferencesManager.updateStrokeColor(strokeColour = colour)
        _uiState.update {
            it.copy(
                strokeColour = preferencesManager.getStrokeColor(),
            )
        }
    }

    fun setImage(bitmap: ImageBitmap?) {
        _backgroundImage.value = bitmap
    }

    private fun selectStrokeWidth(strokeWidth: Int) {
        preferencesManager.updateStrokeWidth(strokeWidth = strokeWidth)
        _uiState.update {
            it.copy(
                strokeWidth = preferencesManager.getStrokeWidth(),
            )
        }
    }

    private fun updateStylusSetting(useStylusOnly: Boolean) {
        preferencesManager.updateStylusOnlySetting(useStylusOnly = useStylusOnly)
        _uiState.update {
            it.copy(
                shouldUseStylusOnly = preferencesManager.getStylusOnlySetting(),
            )
        }
    }

    private fun updatePressureSetting(detectPressure: Boolean) {
        preferencesManager.updatePressureDetectionSetting(detectPressure = detectPressure)
        _uiState.update {
            it.copy(
                shouldDetectPressure = preferencesManager.getPressureDetectionSetting(),
            )
        }
    }

    private fun updateEnableStylusDialog(canShow: Boolean) {
        preferencesManager.updateEnableStylusDialog(canShow = canShow)
        _uiState.update {
            it.copy(
                canShowEnableStylusDialog = preferencesManager.getEnableStylusDialog(),
            )
        }
    }

    private fun updateDisableStylusDialog(canShow: Boolean) {
        preferencesManager.updateDisableStylusDialog(canShow = canShow)
        _uiState.update {
            it.copy(
                canShowDisableStylusDialog = preferencesManager.getDisableStylusDialog(),
            )
        }
    }

    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ArtMakerViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ArtMakerViewModel(
                        preferencesManager = PreferencesManager(preferences = ArtMakerSharedPreferences(context = application)),
                        drawingManager = DrawingManager(),
                        applicationContext = application.applicationContext,
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel Class")
            }
        }
    }
}
