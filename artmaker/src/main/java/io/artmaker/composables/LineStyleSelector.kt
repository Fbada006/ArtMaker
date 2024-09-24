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
package io.artmaker.composables

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import io.artmaker.utils.createPathEffect
import io.fbada006.artmaker.R

@Composable
internal fun LineStyleSelector(selectedStyle: LineStyle, onStyleSelected: (LineStyle) -> Unit, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier,
    ) {
        Text(stringResource(R.string.line_style), style = MaterialTheme.typography.bodyLarge)

        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.art_maker_line_style_selector_spacing)),
        ) {
            LineStyleOption(
                style = LineStyle.FILLED,
                isSelected = selectedStyle == LineStyle.FILLED,
                onClick = { onStyleSelected(LineStyle.FILLED) },
            )
            LineStyleOption(
                style = LineStyle.DASHED,
                isSelected = selectedStyle == LineStyle.DASHED,
                onClick = { onStyleSelected(LineStyle.DASHED) },
            )
            LineStyleOption(
                style = LineStyle.ROUND_DOTTED,
                isSelected = selectedStyle == LineStyle.ROUND_DOTTED,
                onClick = { onStyleSelected(LineStyle.ROUND_DOTTED) },
            )
            LineStyleOption(
                style = LineStyle.SQUARE_DOTTED,
                isSelected = selectedStyle == LineStyle.SQUARE_DOTTED,
                onClick = { onStyleSelected(LineStyle.SQUARE_DOTTED) },
            )
        }
    }
}

@Composable
internal fun LineStyleOption(style: LineStyle, isSelected: Boolean, onClick: () -> Unit) {
    val strokeWidth = 6f
    Card(
        modifier = Modifier
            .size(
                width = dimensionResource(R.dimen.art_maker_line_style_option_width),
                height = dimensionResource(R.dimen.art_maker_line_style_option_height)
            )
            .padding(dimensionResource(R.dimen.art_maker_line_style_option_padding)),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified,
        ),
        onClick = onClick,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(dimensionResource(R.dimen.art_maker_line_style_option_canvas))) {
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
