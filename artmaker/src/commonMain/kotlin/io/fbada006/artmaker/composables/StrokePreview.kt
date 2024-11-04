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
import io.fbada006.artmaker.dimensions.Dimensions
import io.fbada006.artmaker.utils.createPathEffect
import kotlin.math.sin

@Composable
internal fun StrokePreview(state: StrokeState, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                color = Color(248, 246, 240),
                shape = RoundedCornerShape(Dimensions.ArtMakerStrokePreviewShapeSize),
            ),
    ) {
        Canvas(
            modifier = Modifier
                .padding( Dimensions.ArtMakerStrokePreviewPadding)
                .matchParentSize(),
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            drawPath(
                path = createWavePath(canvasWidth, canvasHeight),
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