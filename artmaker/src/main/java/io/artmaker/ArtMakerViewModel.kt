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
import androidx.compose.ui.graphics.Color
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
import io.artmaker.data.PreferenceKeys
import io.artmaker.data.PreferenceKeys.PREF_SELECTED_STROKE_WIDTH
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
    private val artMakerSharedPreferences: ArtMakerSharedPreferences,
    private val drawingManager: DrawingManager,
    private val applicationContext: Context,
) : ViewModel() {

    private var _uiState = MutableStateFlow(
        value = ArtMakerUIState(
            strokeColour = artMakerSharedPreferences.get(
                key = PreferenceKeys.PREF_SELECTED_STROKE_COLOUR,
                defaultValue = Color.Red.toArgb(),
            ),
            strokeWidth = artMakerSharedPreferences.get(
                key = PREF_SELECTED_STROKE_WIDTH,
                defaultValue = 5,
            ),
            shouldUseStylusOnly = artMakerSharedPreferences.get(
                key = PreferenceKeys.PREF_USE_STYLUS_ONLY,
                false,
            ),
            shouldDetectPressure = artMakerSharedPreferences.get(
                key = PreferenceKeys.PREF_DETECT_PRESSURE,
                false,
            ),
            canShowEnableStylusDialog = artMakerSharedPreferences.get(
                key = PreferenceKeys.PREF_SHOW_ENABLE_STYLUS_DIALOG,
                true,
            ),
            canShowDisableStylusDialog = artMakerSharedPreferences.get(
                key = PreferenceKeys.PREF_SHOW_DISABLE_STYLUS_DIALOG,
                true,
            ),
        ),
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
            is ArtMakerAction.SelectStrokeColour -> updateStrokeColor(colour = action.color)
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

    private fun updateStrokeColor(colour: Color) {
        artMakerSharedPreferences.set(
            key = PreferenceKeys.PREF_SELECTED_STROKE_COLOUR,
            value = colour.toArgb(),
        )
        _uiState.update {
            it.copy(
                strokeColour = artMakerSharedPreferences.get(
                    key = PreferenceKeys.PREF_SELECTED_STROKE_COLOUR,
                    defaultValue = 0,
                ),
            )
        }
    }

    fun setImage(bitmap: ImageBitmap?) {
        _backgroundImage.value = bitmap
    }

    private fun selectStrokeWidth(strokeWidth: Int) {
        artMakerSharedPreferences.set(
            key = PREF_SELECTED_STROKE_WIDTH,
            value = strokeWidth,
        )
        _uiState.update {
            it.copy(
                strokeWidth = artMakerSharedPreferences.get(
                    PREF_SELECTED_STROKE_WIDTH,
                    defaultValue = 5,
                ),
            )
        }
    }

    private fun updateStylusSetting(useStylusOnly: Boolean) {
        artMakerSharedPreferences.set(PreferenceKeys.PREF_USE_STYLUS_ONLY, useStylusOnly)
        _uiState.update {
            it.copy(
                shouldUseStylusOnly = artMakerSharedPreferences.get(
                    key = PreferenceKeys.PREF_USE_STYLUS_ONLY,
                    false,
                ),
            )
        }
    }

    private fun updatePressureSetting(detectPressure: Boolean) {
        artMakerSharedPreferences.set(PreferenceKeys.PREF_DETECT_PRESSURE, detectPressure)
        _uiState.update {
            it.copy(
                shouldDetectPressure = artMakerSharedPreferences.get(
                    key = PreferenceKeys.PREF_DETECT_PRESSURE,
                    false,
                ),
            )
        }
    }

    private fun updateEnableStylusDialog(canShow: Boolean) {
        artMakerSharedPreferences.set(PreferenceKeys.PREF_SHOW_ENABLE_STYLUS_DIALOG, canShow)
        _uiState.update {
            it.copy(
                canShowEnableStylusDialog = artMakerSharedPreferences.get(
                    key = PreferenceKeys.PREF_SHOW_ENABLE_STYLUS_DIALOG,
                    true,
                ),
            )
        }
    }

    private fun updateDisableStylusDialog(canShow: Boolean) {
        artMakerSharedPreferences.set(PreferenceKeys.PREF_SHOW_DISABLE_STYLUS_DIALOG, canShow)
        _uiState.update {
            it.copy(
                canShowDisableStylusDialog = artMakerSharedPreferences.get(
                    key = PreferenceKeys.PREF_SHOW_DISABLE_STYLUS_DIALOG,
                    true,
                ),
            )
        }
    }

    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ArtMakerViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ArtMakerViewModel(
                        artMakerSharedPreferences = ArtMakerSharedPreferences(
                            context = application,
                        ),
                        drawingManager = DrawingManager(),
                        applicationContext = application.applicationContext,
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel Class")
            }
        }
    }
}
