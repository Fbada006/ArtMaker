package com.artmaker.models

import androidx.compose.ui.graphics.Color

data class ArtMakerDefaults(
    val strokeWidth: Float = 15f,
    val strokeColor: Color = Color.Unspecified,
    val alpha: Float = 1f,
    val sliderThumbColor: Color = Color.Unspecified,
    val sliderActiveTrackColor: Color = Color.Unspecified,
    val sliderInactiveTickColor:Color =  Color.Unspecified,
    val sliderTextColor: Color = Color.Unspecified
)
