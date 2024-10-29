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

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.graphics.Color
import com.github.ajalt.colormath.model.HSV
import com.github.ajalt.colormath.model.RGB

/**
 * A representation of Color in Hue, Saturation and Value form.
 */
internal data class HsvColor(
    // from = 0.0, to = 360.0
    val hue: Float,
    // from = 0.0, to = 1.0
    val saturation: Float = 1.0f,
    // from = 0.0, to = 1.0
    val value: Float = 1.0f,
) {

    fun toColor(): Color = Color.hsv(hue, saturation, value)

    companion object {
        private fun HSV.toColor(): HsvColor = HsvColor(
            hue = if (this.h.isNaN()) 0f else this.h,
            saturation = this.s,
            value = this.v,
        )

        fun from(color: Color): HsvColor = RGB(
            color.red,
            color.green,
            color.blue,
            color.alpha,
        ).toHSV().toColor()

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
