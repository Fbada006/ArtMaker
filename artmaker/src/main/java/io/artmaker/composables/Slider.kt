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
package io.artmaker.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.artmaker.models.ArtMakerConfiguration
import io.fbada006.artmaker.R

private const val MIN_WIDTH = 1f
private const val MAX_WIDTH = 50f

/**
 * This is a [Slider] that allows the user to select their desired Stroke Width within the specified range.
 */
@Composable
internal fun Slider(sliderPosition: Float, onValueChange: (Float) -> Unit, configuration: ArtMakerConfiguration, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(id = R.string.set_width, sliderPosition.toInt()),
            style = MaterialTheme.typography.titleLarge,
            color = configuration.strokeSliderTextColor,
        )
        Slider(
            value = sliderPosition,
            onValueChange = onValueChange,
            colors = SliderDefaults.colors(
                thumbColor = configuration.strokeSliderThumbColor,
                activeTrackColor = configuration.strokeSliderActiveTrackColor,
                inactiveTickColor = configuration.strokeSliderInactiveTickColor,
            ),
            valueRange = MIN_WIDTH..MAX_WIDTH,
        )
    }
}
