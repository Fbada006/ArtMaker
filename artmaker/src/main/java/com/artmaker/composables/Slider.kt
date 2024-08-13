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
package com.artmaker.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.artmaker.artmaker.R
import com.artmaker.models.ArtMakerConfiguration

private const val MIN_WIDTH = 1f
private const val MAX_WIDTH = 50f

/**
 * This is a [Slider] that allows the user to select their desired Stroke Width within the specified range.
 */
@Composable
internal fun Slider(
    modifier: Modifier = Modifier,
    sliderPosition: Float,
    onValueChange: (Float) -> Unit,
    defaults: ArtMakerConfiguration,
) {
    Column(
        modifier = modifier
            .padding(all = dimensionResource(id = R.dimen.Padding7)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = sliderPosition.toInt().toString(),
            style = MaterialTheme.typography.titleLarge,
            color = defaults.strokeSliderTextColor,
        )
        Slider(
            value = sliderPosition,
            onValueChange = onValueChange,
            colors = SliderDefaults.colors(
                thumbColor = defaults.strokeSliderThumbColor,
                activeTrackColor = defaults.strokeSliderActiveTrackColor,
                inactiveTickColor = defaults.strokeSliderInactiveTickColor,
            ),
            valueRange = MIN_WIDTH..MAX_WIDTH,
        )
    }
}
