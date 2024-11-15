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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import io.fbada006.artmaker.Res
import io.fbada006.artmaker.dimensions.Dimensions
import io.fbada006.artmaker.stroke_preview_box
import io.fbada006.artmaker.stroke_preview_canvas
import io.fbada006.artmaker.utils.createPathEffect
import org.jetbrains.compose.resources.stringResource
import kotlin.math.sin

/**
 * This is the UI that is shown in the stroke settings UI. It acts as a way of ensuring that the user has a real-time preview of how the line drawn
 * on screen will look like.
 *
 * @param state provides the settings for the preview namely color, style, and stroke width
 * @param modifier is the modifier for the preview UI
 */
@Composable
internal fun StrokePreview(state: StrokeState, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                color = Color(248, 246, 240),
                shape = RoundedCornerShape(Dimensions.ArtMakerStrokePreviewShapeSize),
            )
            .testTag(tag = stringResource(resource = Res.string.stroke_preview_box)),
    ) {
        Canvas(
            modifier = Modifier
                .padding(Dimensions.ArtMakerStrokePreviewPadding)
                .matchParentSize()
                .testTag(tag = stringResource(resource = Res.string.stroke_preview_canvas)),
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            drawPath(
                path = createWavePath(canvasWidth, canvasHeight), // The preview will draw a wavy line. Just a style preference
                color = Color(state.strokeColor),
                style = Stroke(
                    width = state.strokeWidth.toFloat(),
                    pathEffect = createPathEffect(style = state.lineStyle, size = state.strokeWidth.toFloat()),
                ),
            )
        }
    }
}

internal data class StrokeState(val strokeColor: Int, val strokeWidth: Int, val lineStyle: LineStyle)

private fun createWavePath(canvasWidth: Float, canvasHeight: Float): Path {
    val path = Path()
    val waveAmplitude = canvasHeight / 4
    val waveFrequency = 0.01f

    path.moveTo(0f, canvasHeight / 2)

    for (x in 0..canvasWidth.toInt()) {
        val y = sin(x * waveFrequency) * waveAmplitude + canvasHeight / 2
        path.lineTo(x.toFloat(), y)
    }

    return path
}
