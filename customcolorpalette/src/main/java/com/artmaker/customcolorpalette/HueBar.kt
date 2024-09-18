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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput

/**
 * Hue side bar Component that changes the hue of a colour.
 *
 * @param modifier modifiers to set to the hue bar.
 * @param currentColor the initial color to set on the hue bar.
 * @param onHueChanged the callback that is invoked when hue value changes. Hue is between 0 - 360.
 */
@Composable
internal fun HueBar(modifier: Modifier = Modifier, currentColor: HsvColor, onHueChanged: (Float) -> Unit) {
    val rainbowBrush = remember {
        Brush.verticalGradient(rainbowColors)
    }
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown()
                    onHueChanged(getHueFromPoint(down.position.y, size.height.toFloat()))
                    drag(down.id) { change ->
                        if (change.position != Offset.Zero) change.consume()
                        onHueChanged(getHueFromPoint(change.position.y, size.height.toFloat()))
                    }
                }
            },
    ) {
        drawRect(rainbowBrush)

        val huePoint = getPointFromHue(color = currentColor, height = this.size.height)
        drawVerticalSelector(huePoint)
    }
}

private val rainbowColors = listOf(
    Color(0xFFFF0040),
    Color(0xFFFF00FF),
    Color(0xFF8000FF),
    Color(0xFF0000FF),
    Color(0xFF0080FF),
    Color(0xFF00FFFF),
    Color(0xFF00FF80),
    Color(0xFF00FF00),
    Color(0xFF80FF00),
    Color(0xFFFFFF00),
    Color(0xFFFF8000),
    Color(0xFFFF0000),
)

private const val MAX_HUE = 360f
private const val MIN_Y = 0f

private fun getPointFromHue(color: HsvColor, height: Float): Float = height - (color.hue * height / MAX_HUE)

private fun getHueFromPoint(y: Float, height: Float): Float {
    val newY = y.coerceIn(MIN_Y, height)
    return MAX_HUE - newY * MAX_HUE / height
}
