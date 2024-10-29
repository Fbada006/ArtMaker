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
package io.fbada006.artmaker.dimensions

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * This is the representation of the Dimensions to be used across all of the platforms.
 */

object Dimensions {

    // ArtMakerControlMenu
    val ArtMakerControlMenuShadowElevation: Dp = 60.dp
    val ArtMakerControlMenuPadding: Dp = 10.dp
    val ColorPickerMenuItemBorderWidth: Dp = 2.dp
    val ColorPickerMenuItemShapeSize: Dp = 32.dp
    val ColorPickerMenuItemPadding: Dp = 2.dp
    val ArtMakerControlMenuDropDownPadding: Dp = 12.dp
    val CustomColorPaletteHeight: Dp = 330.dp
    val CustomColorPalettePadding: Dp = 12.dp
    val MenuItemSize: Dp = 32.dp

    // Color Picker
    val ColorPickerBottomPadding: Dp = 10.dp
    val ColorPickerHorizontalArrangement: Dp = 8.dp
    val FirstColorsRowPadding: Dp = 4.dp
    val FirstColorSetHorizontalArrangement: Dp = 4.dp
    val FirstColorSetVerticalArrangement: Dp = 4.dp
    val RecentColorsTextPadding: Dp = 4.dp
    val SecondColorSetPadding: Dp = 4.dp
    val SecondColorSetHorizontalArrangement: Dp = 4.dp
    val SecondColorSetVerticalArrangement: Dp = 4.dp
    val CustomColorPickerSize: Dp = 48.dp
    val CustomColorPickerShapeSize: Dp = 8.dp
    val ColorItemSize: Dp = 48.dp
    val ColorItemShapeSize: Dp = 8.dp

    // ArtMaker
    val FullScreenEnabledPadding: Dp = 0.dp
    val FullScreenDisabledPadding: Dp = 60.dp
    val ExportArtButtonSpacerHeight: Dp = 4.dp
    val FullScreenToggleButtonSpacerHeight: Dp = 4.dp
    val StrokeSettingsTopPadding: Dp = 8.dp
    val StrokeSettingsEndPadding: Dp = 12.dp
    val StrokeSettingsStartPadding: Dp = 12.dp
    val ArtMakerControlMenuHeight: Dp = 60.dp

    // StrokePreview
    val StrokePreviewShapeSize: Dp = 12.dp
    val StrokePreviewPadding: Dp = 7.dp

    // StrokeSettings
    val StrokePreviewSpacerHeight: Dp = 7.dp
    val LineStyleSelectorSpacerHeight: Dp = 7.dp

    // Line style UI
    val LineStyleSelectorHeight: Dp = 54.dp
    val LineStyleOptionWidth: Dp = 54.dp
    val LineStyleOptionHeight: Dp = 48.dp
    val LineStyleSelectorSpacing: Dp = 8.dp
    val LineStyleOptionPadding: Dp = 4.dp
    val LineStyleOptionCanvas: Dp = 40.dp
}
