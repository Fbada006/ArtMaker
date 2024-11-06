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
package io.fbada006.artmaker.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import io.fbada006.artmaker.Res
import io.fbada006.artmaker.dimensions.Dimensions
import io.fbada006.artmaker.line_style
import io.fbada006.artmaker.utils.createPathEffect
import org.jetbrains.compose.resources.stringResource

/**
 * This is the row that provides a list of different line styles (such as dashed, dotted, filled etc) to be used during drawing. This UI is part
 * of the stroke preview
 *
 * @param selectedStyle is the current line style selected by the user
 * @param onStyleSelected is called once the selection changes
 * @param modifier is the modifier for the selector UI
 */
@Composable
internal fun LineStyleSelector(selectedStyle: LineStyle, onStyleSelected: (LineStyle) -> Unit, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier,
    ) {
        Text(stringResource(Res.string.line_style), style = MaterialTheme.typography.bodyLarge)

        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimensions.ArtMakerLineStyleSelectorSpacing),
        ) {
            LineStyle.entries.forEach { style ->
                LineStyleOption(
                    style = style,
                    isSelected = selectedStyle == style,
                    onClick = { onStyleSelected(style) },
                )
            }
        }
    }
}

/**
 * The item to be used to display the different line styles available for selection as defined in the [LineStyle] enum
 *
 * @param style is the line style
 * @param isSelected whether this style is the current one selected or not
 * @param onClick is triggered when the option is clicked
 */
@Composable
internal fun LineStyleOption(style: LineStyle, isSelected: Boolean, onClick: () -> Unit) {
    val strokeWidth = 6f
    Card(
        modifier = Modifier
            .size(
                width = Dimensions.ArtMakerLineStyleOptionWidth,
                height = Dimensions.ArtMakerLineStyleOptionHeight,
            )
            .padding(Dimensions.ArtMakerLineStyleOptionPadding),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified,
        ),
        onClick = onClick,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(Dimensions.ArtMakerLineStyleOptionCanvas)) {
                val pathEffect = when (style) {
                    LineStyle.FILLED -> null
                    LineStyle.DASHED -> PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    else -> createPathEffect(style, strokeWidth)
                }

                drawLine(
                    color = if (isSelected) Color.White else Color.Black,
                    start = Offset(0f, size.height / 2),
                    end = Offset(size.width, size.height / 2),
                    strokeWidth = strokeWidth,
                    pathEffect = pathEffect,
                )
            }
        }
    }
}

internal enum class LineStyle {
    FILLED,
    DASHED,
    ROUND_DOTTED,
    SQUARE_DOTTED,
}
