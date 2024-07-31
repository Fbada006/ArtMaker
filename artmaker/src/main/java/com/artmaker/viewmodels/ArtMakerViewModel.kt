package com.artmaker.viewmodels

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artmaker.actions.ArtMakerAction
import com.artmaker.sharedpreferences.ArtMakerSharedPreferences
import com.artmaker.sharedpreferences.PreferenceKeys
import com.artmaker.state.ArtMakerUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class ArtMakerViewModel(
    private val preferences: ArtMakerSharedPreferences
) : ViewModel() {

    private var _artMakerUIState =
        MutableStateFlow(value = ArtMakerUIState(strokeColour = preferences.get(PreferenceKeys.SELECTED_STROKE_COLOUR, 0)))
    val artMakerUIState = _artMakerUIState.asStateFlow()

    fun onAction(action: ArtMakerAction) {
        when (action) {
            ArtMakerAction.ExportArt -> exportArt()
            ArtMakerAction.Redo -> redo()
            ArtMakerAction.Undo -> undo()
            ArtMakerAction.Clear -> clear()
            ArtMakerAction.UpdateBackground -> updateBackgroundColour()
            is ArtMakerAction.SelectStrokeColour -> updateStrokeColor(colour = action.color)
            ArtMakerAction.SelectStrokeWidth -> selectStrokeWidth()
        }
    }

    private fun exportArt() {}

    private fun redo() {}

    private fun undo() {}

    private fun clear() {}

    private fun updateBackgroundColour() {}

    private fun updateStrokeColor(colour: Color) {
        preferences.set(
            key = PreferenceKeys.SELECTED_STROKE_COLOUR,
            value = colour.toArgb()
        )
        _artMakerUIState.update {
            it.copy(strokeColour = preferences.get(PreferenceKeys.SELECTED_STROKE_COLOUR, 0))
        }
    }

    private fun selectStrokeWidth() {}

    companion object {
        fun provideFactory(
            context: Context
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ArtMakerViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ArtMakerViewModel(
                        preferences = ArtMakerSharedPreferences(
                            context = context
                        )
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel Class")
            }
        }
    }

}