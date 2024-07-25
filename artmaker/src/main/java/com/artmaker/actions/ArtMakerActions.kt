package com.artmaker.actions

sealed interface ArtMakerActions {

    data object ExportArt : ArtMakerActions

    data object Undo : ArtMakerActions

    data object Redo : ArtMakerActions

    data object Clear : ArtMakerActions

    data object UpdateBackground : ArtMakerActions

    data object SelectColour : ArtMakerActions

    data object SelectStrokeWidth : ArtMakerActions

}