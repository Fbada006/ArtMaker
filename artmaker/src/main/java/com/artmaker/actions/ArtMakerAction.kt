package com.artmaker.actions

sealed interface ArtMakerAction {

    data object ExportArt : ArtMakerAction

    data object Undo : ArtMakerAction

    data object Redo : ArtMakerAction

    data object Clear : ArtMakerAction

    data object UpdateBackground : ArtMakerAction

    data object SelectStrokeColour : ArtMakerAction

    data object SelectStrokeWidth : ArtMakerAction

}