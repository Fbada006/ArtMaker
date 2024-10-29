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

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import io.fbada006.artmaker.DrawScreenState
import io.fbada006.artmaker.models.PointsData
import io.fbada006.artmaker.models.alpha

internal fun toImageBitmap(
    bitmapWidth: Int,
    bitmapHeight: Int,
    state: DrawScreenState,
    isEraserActive: Boolean,
    eraserRadius: Float,
    eraserPosition: Offset?,
    pathList: SnapshotStateList<PointsData>,
): ImageBitmap {
    val imgBitmap = ImageBitmap(bitmapWidth, bitmapHeight)

    Canvas(imgBitmap).apply {
        CanvasDrawScope().draw(
            density = Density(1f, 1f),
            layoutDirection = LayoutDirection.Ltr,
            canvas = this,
            size = Size(bitmapWidth.toFloat(), bitmapHeight.toFloat()),
        ) {
            if (state.backgroundImage != null) {
                val shader = ImageShader(state.backgroundImage, TileMode.Clamp)
                val brush = ShaderBrush(shader)
                drawRect(
                    brush = brush,
                    size = Size(bitmapWidth.toFloat(), bitmapHeight.toFloat()),
                )
            } else {
                drawRect(
                    color = Color(state.backgroundColor),
                    size = Size(bitmapWidth.toFloat(), bitmapHeight.toFloat()),
                )
            }

            pathList.forEach { data ->
                drawPoints(
                    points = data.points,
                    // Draw a point if the shape has only one item otherwise a free flowing shape
                    pointMode = if (data.points.size == 1) PointMode.Points else PointMode.Polygon,
                    color = data.strokeColor,
                    alpha = data.alpha(state.shouldDetectPressure),
                    strokeWidth = data.strokeWidth,
                )
            }
            if (isEraserActive) {
                eraserPosition?.let { position ->
                    drawCircle(
                        color = Color.Gray,
                        radius = eraserRadius,
                        center = position,
                        style = Stroke(
                            width = 8.0f,
                        ),
                    )
                }
            }
        }
    }
    return imgBitmap
}
