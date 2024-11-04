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
import io.fbada006.artmaker.line_style
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import io.fbada006.artmaker.Res
import io.fbada006.artmaker.dimensions.Dimensions
import io.fbada006.artmaker.utils.createPathEffect
import org.jetbrains.compose.resources.stringResource

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
                width = Dimensions.ArtMakerLineStyleOptionWidth,
                height = Dimensions.ArtMakerLineStyleOptionHeight,
            )
            .padding( Dimensions.ArtMakerLineStyleOptionPadding),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified,
        ),
        onClick = onClick,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size( Dimensions.ArtMakerLineStyleOptionCanvas)) {
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

enum class LineStyle {
    FILLED,
    DASHED,
    ROUND_DOTTED,
    SQUARE_DOTTED,
}