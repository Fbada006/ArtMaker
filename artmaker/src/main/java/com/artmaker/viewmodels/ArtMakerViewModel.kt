package com.artmaker.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artmaker.actions.ArtMakerAction
import com.artmaker.sharedpreferences.ArtMakerSharedPreferences
import com.artmaker.sharedpreferences.SharedPreferencesKeys
import com.artmaker.state.ArtMakerUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

internal class ArtMakerViewModel(
    private val artMakerSharedPreferences: ArtMakerSharedPreferences
) : ViewModel() {

    private var _artMakerUIState: MutableStateFlow<ArtMakerUIState> =
        MutableStateFlow(value = ArtMakerUIState())
    val artMakerUIState: StateFlow<ArtMakerUIState> = _artMakerUIState.asStateFlow()

    fun onAction(artMakerAction: ArtMakerAction) {
        when (artMakerAction) {
            ArtMakerAction.ExportArt -> exportArt()
            ArtMakerAction.Redo -> redo(selectedBackgroundColour = generateRandomColor())
            ArtMakerAction.Undo -> undo()
            ArtMakerAction.Clear -> clear()
            ArtMakerAction.UpdateBackground -> updateBackgroundColour()
            ArtMakerAction.SelectStrokeColour -> selectStrokeColour(selectedStrokeColour = generateRandomColor())
            ArtMakerAction.SelectStrokeWidth -> selectStrokeWidth(selectedStrokeWidth = generateRandomColor())
        }
    }

    private fun exportArt() {}

    private fun redo(selectedBackgroundColour: Color) {
        _artMakerUIState.update {
            it.copy(backgroundColour = selectedBackgroundColour.toArgb())
        }
        artMakerSharedPreferences.set(
            key = SharedPreferencesKeys.SELECTED_BACKGROUND_COLOUR,
            value = selectedBackgroundColour.toArgb()
        )
    }

    private fun undo() {
        _artMakerUIState.update {
            it.copy(
                backgroundColour = artMakerSharedPreferences.get(
                    key = SharedPreferencesKeys.SELECTED_BACKGROUND_COLOUR,
                    defaultValue = Color.Blue.toArgb()
                )
            )
        }
    }

    private fun clear() {}

    private fun updateBackgroundColour() {}

    private fun selectStrokeColour(selectedStrokeColour: Color) {
        _artMakerUIState.update {
            it.copy(strokeColour = selectedStrokeColour.toArgb())
        }
        artMakerSharedPreferences.set(
            key = SharedPreferencesKeys.SELECTED_STROKE_COLOUR,
            value = selectedStrokeColour.toArgb()
        )
    }

    private fun selectStrokeWidth(selectedStrokeWidth: Color) {
        _artMakerUIState.update {
            it.copy(strokeWidth = selectedStrokeWidth.toArgb())
        }
        artMakerSharedPreferences.set(
            key = SharedPreferencesKeys.SELECTED_STROKE_WIDTH,
            value = selectedStrokeWidth.toArgb()
        )
    }

    private fun generateRandomColor(): Color {
        val red = Random.nextInt(until = 256)
        val green = Random.nextInt(until = 256)
        val blue = Random.nextInt(until = 256)
        return Color(red = red, green = green, blue = blue)
    }

    companion object {
        fun provideFactory(
            context: Context
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ArtMakerViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ArtMakerViewModel(
                        artMakerSharedPreferences = ArtMakerSharedPreferences(
                            context = context
                        )
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel Class")
            }
        }
    }

}