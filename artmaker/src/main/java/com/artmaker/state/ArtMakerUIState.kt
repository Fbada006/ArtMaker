package com.artmaker.state

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/**
 * Determines the UI state
 */
data class ArtMakerUIState(
    val backgroundColour: Int = Color.White.toArgb(),
    val strokeWidth: Int = 3,
    val strokeColour: Int
)