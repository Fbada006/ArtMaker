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
package io.fbada006.customcolorpalette

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

internal fun DrawScope.drawVerticalSelector(amount: Float) {
    val halfIndicatorThickness = 4.dp.toPx()
    val strokeThickness = 1.dp.toPx()

    val offset =
        Offset(
            y = amount - halfIndicatorThickness,
            x = -strokeThickness,
        )
    val selectionSize = Size(this.size.width + strokeThickness * 2, halfIndicatorThickness * 2f)
    drawSelectorIndicator(
        offset = offset,
        selectionSize = selectionSize,
        strokeThicknessPx = strokeThickness,
    )
}

internal fun DrawScope.drawSelectorIndicator(offset: Offset, selectionSize: Size, strokeThicknessPx: Float) {
    val selectionStyle = Stroke(strokeThicknessPx)
    drawRect(
        Color.Gray,
        topLeft = offset,
        size = selectionSize,
        style = selectionStyle,
    )
    drawRect(
        Color.White,
        topLeft = offset + Offset(strokeThicknessPx, strokeThicknessPx),
        size = selectionSize.inset(2 * strokeThicknessPx),
        style = selectionStyle,
    )
}

internal fun Size.inset(amount: Float): Size = Size(width - amount, height - amount)
