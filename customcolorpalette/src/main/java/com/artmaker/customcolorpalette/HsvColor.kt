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

    fun toColor(): Color = Color.hsv(hue, saturation, value)

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
            },
        )
    }
}
