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
package io.fbada006.artmaker.customcolorpalette

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

/**
 * Saturation Value area Component that changes the saturation of the current color.
 *
 * @param modifier modifiers to set on the Alpha Bar
 * @param currentColor the initial color to set on the alpha bar.
 * @param onSaturationValueChanged the callback that is invoked when saturation or value component of the changes.
 */

@Composable
internal fun SaturationValueArea(
    modifier: Modifier = Modifier,
    currentColor: HsvColor,
    onSaturationValueChanged: (saturation: Float, value: Float) -> Unit,
) {
    val blackGradientBrush = remember {
        Brush.verticalGradient(listOf(Color.White, Color.Black))
    }

    val currentColorGradientBrush = remember(currentColor.hue) {
        val hsv = HsvColor(currentColor.hue)
        Brush.horizontalGradient(
            listOf(Color.White, hsv.toColor()),
        )
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown()
                    val (s, v) = getSaturationPoint(down.position, size)
                    onSaturationValueChanged(s, v)
                    drag(down.id) { change ->
                        if (change.position != Offset.Zero) change.consume()
                        val (newSaturation, newValue) = getSaturationPoint(change.position, size)
                        onSaturationValueChanged(newSaturation, newValue)
                    }
                }
            },
    ) {
        drawRect(blackGradientBrush)
        drawRect(currentColorGradientBrush, blendMode = BlendMode.Modulate)

        drawCircleSelector(currentColor)
    }
}

private fun DrawScope.drawCircleSelector(currentColor: HsvColor) {
    val outerCircleRadius = 6.dp
    val innerCircleRadius = 4.dp
    val outerCircleStrokeWidth = 2.dp
    val innerCircleStrokeWidth = 1.dp

    val point = getSaturationValuePoint(currentColor, size = size)
    val outerCircleStyle = Stroke(outerCircleStrokeWidth.toPx())
    val innerCircleStyle = Stroke(innerCircleStrokeWidth.toPx())

    drawCircle(
        color = Color.White,
        radius = outerCircleRadius.toPx(),
        center = point,
        style = outerCircleStyle,
    )
    drawCircle(
        color = Color.Gray,
        radius = innerCircleRadius.toPx(),
        center = point,
        style = innerCircleStyle,
    )
}

private fun getSaturationPoint(offset: Offset, size: IntSize): Pair<Float, Float> {
    val (saturation, value) = getSaturationValueFromPosition(offset, size.toSize())
    return saturation to value
}

/**
 * Gets the X/Y offset for a color based on the input Size
 *
 * @return an Offset within the Size that represents the saturation and value of the supplied Color.
 */
private fun getSaturationValuePoint(color: HsvColor, size: Size): Offset {
    val colorMax = 1f
    val height: Float = size.height
    val width: Float = size.width

    return Offset((color.saturation * width), (colorMax - color.value) * height)
}

/**
 * Given an offset and size, this function calculates a saturation and value amount based on that.
 *
 * @return new saturation and value
 */
private fun getSaturationValueFromPosition(offset: Offset, size: Size): Pair<Float, Float> {
    val minXY = 0f
    val minSaturation = 0f
    val maxSaturation = 1f
    val colorMax = 1f

    val width = size.width
    val height = size.height

    val newX = offset.x.coerceIn(minXY, width)

    val newY = offset.y.coerceIn(minXY, size.height)
    val saturation = newX / width
    val value = colorMax - (newY / height)

    return saturation.coerceIn(minSaturation, maxSaturation) to value.coerceIn(minSaturation, maxSaturation)
}
