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
package com.fbada006.shared

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fbada006.shared.actions.ArtMakerAction
import com.fbada006.shared.actions.DrawEvent
import com.fbada006.shared.actions.ExportType
import com.fbada006.shared.data.CustomColorsManager
import com.fbada006.shared.data.PreferencesManager
import com.fbada006.shared.drawing.DrawingManager
import com.fbada006.shared.models.PointsData
import com.fbada006.shared.utils.ColorUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ArtMakerViewModel(
    private val customColorsManager: CustomColorsManager,
    private val preferencesManager: PreferencesManager,
    private val drawingManager: DrawingManager,
) : ViewModel() {

    private var _uiState = MutableStateFlow(ArtMakerUIState())
    val uiState = _uiState.asStateFlow()

    val pathList: SnapshotStateList<PointsData> = drawingManager.pathList

    private val _shouldTriggerArtExport = MutableStateFlow(false)
    val shouldTriggerArtExport: StateFlow<Boolean> = _shouldTriggerArtExport

    private val _backgroundImage: MutableState<ImageBitmap?> = mutableStateOf(null)
    val backgroundImage: State<ImageBitmap?> = _backgroundImage

    private val exportType = mutableStateOf<ExportType>(ExportType.FinishDrawingImage)

    private val _finishedImage = MutableStateFlow<ImageBitmap?>(null)
    val finishedImage = _finishedImage.asStateFlow()

    init {
        init()
    }

    fun onAction(action: ArtMakerAction) {
        when (action) {
            is ArtMakerAction.TriggerArtExport -> triggerArtExport(action.type)
            is ArtMakerAction.ExportArt -> exportArt(action.bitmap)
            ArtMakerAction.UpdateBackground -> updateBackgroundColour()
            is ArtMakerAction.SelectStrokeColour -> updateStrokeColor(colour = action.color, isCustomColour = action.isCustomColor)
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

    private fun init() {
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

        viewModelScope.launch {
            preferencesManager.state
                .collectLatest { newState ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            strokeColour = newState.strokeColour,
                            strokeWidth = newState.strokeWidth,
                            shouldUseStylusOnly = newState.shouldUseStylusOnly,
                            shouldDetectPressure = newState.shouldDetectPressure,
                            canShowEnableStylusDialog = newState.canShowEnableStylusDialog,
                            canShowDisableStylusDialog = newState.canShowDisableStylusDialog
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
            if (exportType.value == ExportType.FinishDrawingImage) {
                // At this point, the drawing done and we are going to share it programmatically
                _finishedImage.update { bitmap }
                return@launch
            }
//            val uri = bmp.saveToDisk(applicationContext)
//            shareBitmap(applicationContext, uri)
//        }
        }
    }

    private fun updateBackgroundColour() {}

    private fun updateStrokeColor(colour: Color, isCustomColour: Boolean) {
        viewModelScope.launch {
            // Save a colour only if it is custom and does not exist in defaults
            if (isCustomColour && !ColorUtils.COLOR_PICKER_DEFAULT_COLORS.contains(colour)) customColorsManager.saveColor(colour.toArgb())
            preferencesManager.updateStrokeColor(strokeColour = colour.toArgb())
        }
    }

    fun setImage(bitmap: ImageBitmap?) {
        _backgroundImage.value = bitmap
    }

    private fun selectStrokeWidth(strokeWidth: Int) {
        viewModelScope.launch {
            preferencesManager.updateStrokeWidth(strokeWidth = strokeWidth)
        }
    }

    private fun updateStylusSetting(useStylusOnly: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateStylusOnlySetting(useStylusOnly = useStylusOnly)
        }
    }

    private fun updatePressureSetting(detectPressure: Boolean) {
        viewModelScope.launch {
            preferencesManager.updatePressureDetectionSetting(detectPressure = detectPressure)
        }
    }

    private fun updateEnableStylusDialog(canShow: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateEnableStylusDialog(canShow = canShow)
        }
    }

    private fun updateDisableStylusDialog(canShow: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateDisableStylusDialog(canShow = canShow)
        }
    }
}
