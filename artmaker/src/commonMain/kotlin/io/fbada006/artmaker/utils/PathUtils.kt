@file:JvmName(name = "PathUtils")

package io.fbada006.artmaker.utils
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StampedPathEffectStyle
import io.fbada006.artmaker.composables.LineStyle
import kotlin.jvm.JvmName

private fun createCircleStamp(size: Float): Path = Path().apply {
    addOval(Rect(0f, 0f, size, size))
}

private fun createSquareStamp(size: Float): Path = Path().apply {
    addRect(Rect(0f, 0f, size, size))
}

internal fun createPathEffect(style: LineStyle, size: Float): PathEffect? {
    val stamp = when (style) {
        LineStyle.ROUND_DOTTED -> createCircleStamp(size)
        LineStyle.SQUARE_DOTTED -> createSquareStamp(size)
        LineStyle.DASHED -> return PathEffect.dashPathEffect(floatArrayOf(65f, 10f), 0f)
        LineStyle.FILLED -> return null
    }

    return PathEffect.stampedPathEffect(
        shape = stamp,
        advance = size * 1.5f,
        phase = 0f,
        style = StampedPathEffectStyle.Rotate,
    )
}

internal fun List<Offset>.toPath(): Path {
    if (this.isEmpty()) return Path()
    val offsets = this

    return Path().apply {
        moveTo(offsets.first().x, offsets.first().y)
        for (i in 1 until offsets.size) {
            lineTo(offsets[i].x, offsets[i].y)
        }
    }
}