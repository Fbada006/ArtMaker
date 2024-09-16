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
package com.artmaker.customcolorpalette

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import io.fbada006.artmaker.customcolorpalette.R

/**
 * The main entry point for the palette to create a custom colour aside from the default ones
 *
 * @param onCancel called when custom color creation is cancelled
 * @param onAccept called when a new color has been created and ready to be used
 * @param modifier is the Modifier
 */
@Composable
fun CustomColorPalette(
    onCancel: () -> Unit,
    onAccept: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorPickerValueState = rememberSaveable(stateSaver = HsvColor.Saver) {
        mutableStateOf(HsvColor.from(Color.Red))
    }

    Row(modifier = modifier) {
        val barThickness = dimensionResource(R.dimen.dimen32)
        val paddingBetweenBars = dimensionResource(R.dimen.dimen8)
        Column(modifier = Modifier.weight(0.8f)) {
            SaturationValueArea(
                modifier = Modifier.weight(1f),
                currentColor = colorPickerValueState.value,
                onSaturationValueChanged = { saturation, value ->
                    colorPickerValueState.value =
                        colorPickerValueState.value.copy(saturation = saturation, value = value)
                },
            )

            Spacer(modifier = Modifier.height(paddingBetweenBars))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(barThickness)
                    .background(colorPickerValueState.value.toColor()),
            )

            Spacer(modifier = Modifier.height(paddingBetweenBars))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(onClick = onCancel) {
                    Text(text = stringResource(android.R.string.cancel))
                }
                Spacer(Modifier.width(dimensionResource(R.dimen.dimen12)))
                Button(onClick = { onAccept(colorPickerValueState.value.toColor()) }) {
                    Text(text = stringResource(android.R.string.ok))
                }
            }
        }
        Spacer(modifier = Modifier.width(paddingBetweenBars))
        HueBar(
            modifier = Modifier
                .width(barThickness)
                .fillMaxHeight(),
            currentColor = colorPickerValueState.value,
            onHueChanged = { newHue ->
                colorPickerValueState.value = colorPickerValueState.value.copy(hue = newHue)
            },
        )
    }
}
