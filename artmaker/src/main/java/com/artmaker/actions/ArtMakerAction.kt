package com.artmaker.actions

import androidx.compose.ui.graphics.Color

/**
 * Define all of the user's actions
 */
sealed interface ArtMakerAction {

    data object ExportArt : ArtMakerAction

    data object Undo : ArtMakerAction

    data object Redo : ArtMakerAction

    data object Clear : ArtMakerAction

    data object UpdateBackground : ArtMakerAction

    data class SelectStrokeColour(val color: Color) : ArtMakerAction

    data object SelectStrokeWidth : ArtMakerAction

}