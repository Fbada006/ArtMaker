package com.artmaker.state

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/**
 * This is a Data Class that represents the UI State.
 * Dummy values have been used for testing purposes but they are of course subject to change.
 */
data class ArtMakerUIState(
    val backgroundColour: Int = Color.Blue.toArgb(),
    val strokeWidth: Int = 3,
    val strokeColour: Int = Color.Red.toArgb()
)