package com.artmaker.actions

/**
 * This is a Sealed Interface that defines all of the user's actions (Intents) to be implemented by ArtMakerViewModel...
 */
sealed interface ArtMakerAction {

    data object ExportArt : ArtMakerAction

    data object Undo : ArtMakerAction

    data object Redo : ArtMakerAction

    data object Clear : ArtMakerAction

    data object UpdateBackground : ArtMakerAction

    data object SelectStrokeColour : ArtMakerAction

    data object SelectStrokeWidth : ArtMakerAction

}