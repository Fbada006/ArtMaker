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
package io.fbada006.artmaker.actions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import io.fbada006.artmaker.composables.LineStyle

/**
 * Define all of the user's actions
 */
sealed interface ArtMakerAction {
    data class TriggerArtExport(val type: ExportType) : ArtMakerAction
    data class ExportArt(val bitmap: ImageBitmap?) : ArtMakerAction
    data object UpdateBackground : ArtMakerAction
    data class SelectStrokeColour(val color: Color, val isCustomColor: Boolean = false) : ArtMakerAction
    data class SetStrokeWidth(val strokeWidth: Int) : ArtMakerAction
    data class UpdateStylusAvailability(val isStylusAvailable: Boolean) : ArtMakerAction
    data class UpdateSetStylusOnly(val shouldUseStylusOnly: Boolean) : ArtMakerAction
    data class UpdateSetPressureDetection(val shouldDetectPressure: Boolean) : ArtMakerAction
    class UpdateEnableStylusDialogShow(val canShowEnableStylusDialog: Boolean) : ArtMakerAction
    class UpdateDisableStylusDialogShow(val canShowDisableStylusDialog: Boolean) : ArtMakerAction
    data class SetLineStyle(val style: LineStyle) : ArtMakerAction
}

sealed interface ExportType {
    data object ShareImage : ExportType
    data object FinishDrawingImage : ExportType
}
