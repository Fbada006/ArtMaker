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
package io.fbada006.artmaker.utils

import androidx.compose.ui.graphics.Color
import io.fbada006.artmaker.composables.LUMINANCE_THRESHOLD
import kotlin.math.pow

internal object ColorUtils {

    /**
     * Calculates the perceived brightness (luminance) of a color using the same formula
     * as W3C for WCAG 2.0. Returns a value between 0 (darkest) and 1 (brightest).
     *
     * @param color The color as Int (ARGB format)
     * @return luminance value between 0 and 1
     */
    private fun calculateLuminance(color: Int): Double {
        // Extract RGB components
        val red = (color shr 16 and 0xff) / 255.0
        val green = (color shr 8 and 0xff) / 255.0
        val blue = (color and 0xff) / 255.0

        // Convert to linear RGB values
        val r = if (red <= 0.03928) red / 12.92 else ((red + 0.055) / 1.055).pow(2.4)
        val g = if (green <= 0.03928) green / 12.92 else ((green + 0.055) / 1.055).pow(2.4)
        val b = if (blue <= 0.03928) blue / 12.92 else ((blue + 0.055) / 1.055).pow(2.4)

        // Calculate luminance using WCAG formula
        return 0.2126 * r + 0.7152 * g + 0.0722 * b
    }

    /**
     * Determines if a color should be considered "light" or "dark"
     * based on its luminance value.
     *
     * @param color The color as Int (ARGB format)
     * @param threshold The luminance threshold (default is 0.5)
     * @return true if the color is considered light, false if dark
     */
    internal fun isLightColor(color: Int, threshold: Double = LUMINANCE_THRESHOLD): Boolean = calculateLuminance(color) > threshold

    internal val COLOR_PICKER_DEFAULT_COLORS = listOf(
        Color.Red,
        Color.Blue,
        Color.Black,
        Color.Yellow,
        Color.Cyan,
        Color.Gray,
        Color.Green,
        Color.Magenta,
        Color.DarkGray,
        Color(color = 0xFFA020F0), // A purple
    )
}
