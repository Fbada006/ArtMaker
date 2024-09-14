package com.artmaker.customcolorpalette

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import android.graphics.Color as AndroidColor

/**
 * A representation of Color in Hue, Saturation and Value form.
 */
data class HsvColor(
    // from = 0.0, to = 360.0
    val hue: Float,
    // from = 0.0, to = 1.0
    val saturation: Float,
    // from = 0.0, to = 1.0
    val value: Float,
) {

    fun toColor(): Color {
        return Color.hsv(hue, saturation, value)
    }

    companion object {
        fun from(color: Color): HsvColor {
            val hsvArr = FloatArray(3)
            AndroidColor.colorToHSV(color.toArgb(), hsvArr)
            return HsvColor(hsvArr[0], hsvArr[1], hsvArr[2])
        }

        val Saver: Saver<HsvColor, *> = listSaver(
            save = {
                listOf(
                    it.hue,
                    it.saturation,
                    it.value,
                )
            },
            restore = {
                HsvColor(
                    it[0], // Hue
                    it[1], // Saturation
                    it[2], // Value
                )
            }
        )
    }
}