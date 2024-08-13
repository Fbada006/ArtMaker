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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.artmaker.actions.ArtMakerAction
import com.artmaker.artmaker.R
import com.artmaker.models.ArtMakerConfiguration
import com.artmaker.state.ArtMakerUIState

/**
 * This is a popup that displays the ArtMakerStrokeWidthSlider and accompanying title.
 */
@Composable
internal fun StrokeWidthSlider(
    state: ArtMakerUIState,
    onAction: (ArtMakerAction) -> Unit,
    isVisible: Boolean,
    artMakerConfiguration: ArtMakerConfiguration,
) {
    var sliderPosition by remember { mutableIntStateOf(value = state.strokeWidth) }
    AnimatedVisibility(visible = isVisible) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = artMakerConfiguration.strokeSliderBackgroundColor)
                .padding(top = dimensionResource(id = R.dimen.Padding4)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(
                text = stringResource(id = R.string.set_width),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Slider(
                sliderPosition = sliderPosition.toFloat(),
                onValueChange = {
                    sliderPosition = it.toInt()
                    onAction(ArtMakerAction.SelectStrokeWidth(strokeWidth = sliderPosition))
                },
                defaults = artMakerConfiguration,
            )
        }
    }
}
