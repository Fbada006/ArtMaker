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
            state.backgroundImage?.let { bitmap ->
                val shader = ImageShader(bitmap, TileMode.Clamp)
                val brush = ShaderBrush(shader)
                drawRect(
                    brush = brush,
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