package com.artmaker.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * This is a [Slider] that allows the user to select their desired Stroke Width withing the specified range.
 */

@Composable
internal fun ArtMakerStrokeWidthSlider(
    modifier: Modifier = Modifier,
    sliderPosition: Float,
    onValueChange: (Float) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(all = 7.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = sliderPosition.toInt().toString(),
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )
        Slider(
            value = sliderPosition,
            onValueChange = onValueChange,
            colors = SliderDefaults.colors(
                thumbColor = Color.Black,
                activeTrackColor = Color.DarkGray,
                inactiveTickColor = Color.LightGray,
            ),
            valueRange = 5f..30f,
        )
    }
}