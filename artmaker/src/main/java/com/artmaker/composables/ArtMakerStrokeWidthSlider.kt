package com.artmaker.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaker.state.ArtMakerUIState

@Composable
internal fun ArtMakerStrokeWidthSlider(
    modifier: Modifier = Modifier,
    artMakerUIState: ArtMakerUIState,
) {
    var sliderPosition by remember { mutableIntStateOf(value = artMakerUIState.strokeWidth) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 7.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = sliderPosition.toString(),
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )
        Slider(
            value = sliderPosition.toFloat(),
            onValueChange = {
                sliderPosition =
                    it.toInt()
            },
            colors = SliderDefaults.colors(
                thumbColor = Color.Black,
                activeTrackColor = Color.DarkGray,
                inactiveTickColor = Color.LightGray,
            ),
            valueRange = 5f..30f,
        )
    }
}