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
package io.fbada006.artmaker.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.fbada006.artmaker.Res
import io.fbada006.artmaker.actions.ArtMakerAction
import io.fbada006.artmaker.dimensions.Dimensions
import io.fbada006.artmaker.enable_pressure_detection
import io.fbada006.artmaker.models.ArtMakerConfiguration
import io.fbada006.artmaker.use_stylus_only
import org.jetbrains.compose.resources.stringResource

/**
 * This is a collection of settings used to control the behaviour of the drawing, such as Stylus Usage or Pressure Detection.
 *
 * @param strokeWidth
 * @param shouldUseStylusOnly whether non stylus input should be ignored
 * @param shouldDetectPressure whether pressure detection is enabled or not
 * @param isStylusAvailable whether device has a stylus or not
 * @param onAction triggered from a user action
 * @param configuration is the global config
 * @param modifier applied to the top most component
 *
 */
@Composable
internal fun StrokeSettings(
    state: StrokeState,
    strokeWidth: Int,
    shouldUseStylusOnly: Boolean,
    shouldDetectPressure: Boolean,
    isStylusAvailable: Boolean,
    onAction: (ArtMakerAction) -> Unit,
    configuration: ArtMakerConfiguration,
    modifier: Modifier = Modifier,
) {
    var sliderPosition by remember { mutableIntStateOf(strokeWidth) }
    var stylusOnly by remember { mutableStateOf(shouldUseStylusOnly) }
    var detectPressure by remember { mutableStateOf(shouldDetectPressure) }
    var lineStyle by remember { mutableStateOf(state.lineStyle) }

    Column(modifier = modifier, verticalArrangement = Arrangement.SpaceEvenly) {
        StrokePreview(
            state = state,
            modifier = Modifier
                .height( Dimensions.ArtMakerStrokePreviewHeight)
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height( Dimensions.ArtMakerStrokePreviewSpacerHeight))
        HorizontalDivider()

        LineStyleSelector(
            lineStyle,
            onStyleSelected = {
                lineStyle = it
                onAction(ArtMakerAction.SetLineStyle(style = lineStyle))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.ArtMakerLineStyleSelectorHeight),
        )
        Spacer(modifier = Modifier.height(Dimensions.ArtMakerStrokePreviewSpacerHeight))
        HorizontalDivider()

        Slider(
            sliderPosition = sliderPosition.toFloat(),
            onValueChange = {
                sliderPosition = it.toInt()
                onAction(ArtMakerAction.SetStrokeWidth(strokeWidth = sliderPosition))
            },
            configuration = configuration,
            modifier = Modifier.fillMaxWidth(),
        )
        // Only show these setting if there is a stylus available
        if (isStylusAvailable) {
            HorizontalDivider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.use_stylus_only),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Switch(
                    checked = stylusOnly,
                    onCheckedChange = {
                        stylusOnly = !stylusOnly
                        onAction(ArtMakerAction.UpdateSetStylusOnly(shouldUseStylusOnly = stylusOnly))
                    },
                )
            }
            HorizontalDivider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.enable_pressure_detection),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Switch(
                    checked = detectPressure,
                    onCheckedChange = {
                        detectPressure = !detectPressure
                        onAction(ArtMakerAction.UpdateSetPressureDetection(shouldDetectPressure = detectPressure))
                    },
                )
            }
        }
    }
}
