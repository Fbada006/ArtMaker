package com.artmaker.controllers

import androidx.lifecycle.ViewModel
import com.artmaker.actions.ArtMakerActions
import com.artmaker.sharedpreferences.ArtMakerSharedPreferences
import com.artmaker.state.ArtMakerUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ArtMakerController(
    private val artMakerSharedPreferences: ArtMakerSharedPreferences
) : ViewModel() {

    private var _artMakerUIState: MutableStateFlow<ArtMakerUIState> = MutableStateFlow(value = ArtMakerUIState())
    val artMakerUIState: StateFlow<ArtMakerUIState> = _artMakerUIState.asStateFlow()

    fun onAction(artMakerActions: ArtMakerActions) {
        when(artMakerActions) {
            ArtMakerActions.ExportArt -> exportArt()
            ArtMakerActions.Redo -> redo()
            ArtMakerActions.Undo -> undo()
            ArtMakerActions.Clear -> clear()
            ArtMakerActions.UpdateBackground -> updateBackground()
            ArtMakerActions.SelectColour -> selectColour()
            ArtMakerActions.SelectStrokeWidth -> selectStrokeWidth()
        }
    }

    private fun exportArt() {}

    private fun redo() {}

    private fun undo() {}

    private fun clear() {}

    private fun updateBackground() {}

    private fun selectColour() {}

    private fun selectStrokeWidth() {}

}