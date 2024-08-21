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
package io.artmaker.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
/**
 * ArtMakerConfiguration holding custom configuration for the ArtMaker Composable
 * @param strokeSliderThumbColor  thumb color when enabled
 * @param strokeSliderActiveTrackColor color of the track in the part that is "active", meaning that the thumb is ahead of it
 * @param strokeSliderInactiveTickColor colors to be used to draw tick marks on the inactive track, if steps are specified on the Slider.
 * @param strokeSliderTextColor color of the current slider value
 * @param pickerCustomColors  A list of custom colors that can be selected to draw on the canvas.
 * @param canvasBackgroundColor the background color of the canvas draw area
 * @param controllerBackgroundColor the controller background color scheme
 * @param strokeSliderBackgroundColor the stoke Slider Surface background color
 * @param canShareArt A Boolean flag indicating whether the feature to share art is enabled or not
 */
data class ArtMakerConfiguration(
    val strokeSliderThumbColor: Color = Color.Unspecified,
    val strokeSliderActiveTrackColor: Color = Color.Unspecified,
    val strokeSliderInactiveTickColor: Color = Color.Unspecified,
    val strokeSliderTextColor: Color = Color.Unspecified,
    val pickerCustomColors: List<Color> = emptyList(),
    val canvasBackgroundColor: Int = Color.White.toArgb(),
    val controllerBackgroundColor: Color = Color.Unspecified,
    val strokeSliderBackgroundColor: Color = Color.Unspecified,
    val canShareArt: Boolean = false
)
