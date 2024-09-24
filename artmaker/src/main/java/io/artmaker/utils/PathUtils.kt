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
@file:JvmName(name = "PathUtils")

package io.artmaker.utils

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StampedPathEffectStyle
import io.artmaker.composables.LineStyle

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
