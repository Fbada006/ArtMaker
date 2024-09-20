package io.artmaker.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import io.fbada006.artmaker.R
import kotlin.math.sin

@Composable
internal fun StrokePreview(state: StrokeState, modifier: Modifier = Modifier) {
    var wavyLineOffsets by remember { mutableStateOf(listOf<Offset>()) }

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .padding(vertical = dimensionResource(R.dimen.Padding7))
                .matchParentSize(),
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            wavyLineOffsets = wavyOffsets(canvasWidth, canvasHeight)

            wavyLineOffsets.zipWithNext { current, next ->
                drawLine(
                    color = Color(state.strokeColor),
                    start = current,
                    end = next,
                    strokeWidth = state.strokeWidth.toFloat(),
                )
            }
        }
    }
}

internal data class StrokeState(
    val strokeColor: Int,
    val strokeWidth: Int,
)

private fun wavyOffsets(canvasWidth: Float, canvasHeight: Float): List<Offset> {
    val waveAmplitude = canvasHeight / 4
    val waveFrequency = 0.01f

    return List(canvasWidth.toInt()) { x ->
        val y = sin(x * waveFrequency) * waveAmplitude + canvasHeight / 2
        Offset(x.toFloat(), y)
    }
}