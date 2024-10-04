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

//import android.app.Application
//import android.content.Context
//import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
//import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
//    private val applicationContext: Context,
) : ViewModel() {

    private var _uiState = MutableStateFlow(ArtMakerUIState())
    val uiState = _uiState.asStateFlow()

    val pathList: SnapshotStateList<PointsData> = drawingManager.pathList

    private val _shouldTriggerArtExport = MutableStateFlow(false)
    val shouldTriggerArtExport: StateFlow<Boolean> = _shouldTriggerArtExport

    private val _backgroundImage: MutableState<ImageBitmap?> = mutableStateOf(null)
    val backgroundImage: State<ImageBitmap?> = _backgroundImage

    private val exportType = mutableStateOf<ExportType>(ExportType.FinishDrawingImage)

//    private val _finishedImage = MutableStateFlow<Bitmap?>(null)
//    val finishedImage = _finishedImage.asStateFlow()

    init {
        listenToUndoRedoState()
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
//        _shouldTriggerArtExport.update { false }
//        viewModelScope.launch {
//            val bmp = bitmap.asAndroidBitmap()
//            if (exportType.value == ExportType.FinishDrawingImage) {
//                // At this point, the drawing done and we are going to share it programmatically
//                _finishedImage.update { bmp }
//                return@launch
//            }
//            val uri = bmp.saveToDisk(applicationContext)
//            shareBitmap(applicationContext, uri)
//        }
    }

    private fun updateBackgroundColour() {}

    private fun updateStrokeColor(colour: Color, isCustomColour: Boolean) {
        viewModelScope.launch {
            // Save a colour only if it is custom and does not exist in defaults
            if (isCustomColour && !ColorUtils.COLOR_PICKER_DEFAULT_COLORS.contains(colour)) customColorsManager.saveColor(colour.toArgb())
            preferencesManager.updateStrokeColor(strokeColour = colour.toArgb())
            preferencesManager.getStrokeColor().collect { strokeColor->
                _uiState.update {
                    it.copy(strokeColour = strokeColor)
                }
            }
        }
    }

    fun setImage(bitmap: ImageBitmap?) {
        _backgroundImage.value = bitmap
    }

    private fun selectStrokeWidth(strokeWidth: Int) {
        viewModelScope.launch {
            preferencesManager.updateStrokeWidth(strokeWidth = strokeWidth)
            preferencesManager.getStrokeWidth().collect { stroke->
                _uiState.update {
                    it.copy(strokeWidth = stroke)
                }
            }
        }
    }

    private fun updateStylusSetting(useStylusOnly: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateStylusOnlySetting(useStylusOnly = useStylusOnly)
            preferencesManager.getStylusOnlySetting().collect { stylus->
                _uiState.update {
                    it.copy(shouldUseStylusOnly = stylus)
                }
            }
        }
    }

    private fun updatePressureSetting(detectPressure: Boolean) {
        viewModelScope.launch {
            preferencesManager.updatePressureDetectionSetting(detectPressure = detectPressure)
            preferencesManager.getPressureDetectionSetting().collect { pressure->
                _uiState.update {
                    it.copy(shouldDetectPressure = pressure)
                }
            }
        }
    }

    private fun updateEnableStylusDialog(canShow: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateEnableStylusDialog(canShow = canShow)
            preferencesManager.getEnableStylusDialog().collect { stylusDialog->
                _uiState.update {
                    it.copy(canShowEnableStylusDialog = stylusDialog)
                }
            }
        }

    }

    private fun updateDisableStylusDialog(canShow: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateDisableStylusDialog(canShow = canShow)
            preferencesManager.getDisableStylusDialog().collect { disableStylusDialog->
                _uiState.update {
                    it.copy(canShowDisableStylusDialog = disableStylusDialog)
                }
            }
        }
    }

//    companion object {
//        fun provideFactory(application: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                if (modelClass.isAssignableFrom(ArtMakerViewModel::class.java)) {
//                    @Suppress("UNCHECKED_CAST")
//                    return ArtMakerViewModel(
//                        preferencesManager = PreferencesManager(preferences = ArtMakerSharedPreferences(context = application)),
//                        customColorsManager = CustomColorsManager(application),
//                        drawingManager = DrawingManager(),
//                        applicationContext = application.applicationContext,
//                    ) as T
//                }
//                throw IllegalArgumentException("Unknown ViewModel Class")
//            }
//        }
//    }
}
