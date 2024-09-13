package io.artmaker.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import io.artmaker.ArtMakerUIState

/**
 * PreferencesManager is used to handle the [ArtMakerSharedPreferences] functionalities and abstract them from [ArtMakerViewModel].
 */

internal class PreferencesManager(private val preferences: ArtMakerSharedPreferences) {

    fun loadInitialUIState(): ArtMakerUIState {
        return ArtMakerUIState(
            strokeColour = preferences.get(
                key = PreferenceKeys.PREF_SELECTED_STROKE_COLOUR,
                defaultValue = Color.Red.toArgb(),
            ),
            strokeWidth = preferences.get(
                key = PreferenceKeys.PREF_SELECTED_STROKE_WIDTH,
                defaultValue = 5,
            ),
            shouldUseStylusOnly = preferences.get(
                key = PreferenceKeys.PREF_USE_STYLUS_ONLY,
                false,
            ),
            shouldDetectPressure = preferences.get(
                key = PreferenceKeys.PREF_DETECT_PRESSURE,
                false,
            ),
            canShowEnableStylusDialog = preferences.get(
                key = PreferenceKeys.PREF_SHOW_ENABLE_STYLUS_DIALOG,
                true,
            ),
            canShowDisableStylusDialog = preferences.get(
                key = PreferenceKeys.PREF_SHOW_DISABLE_STYLUS_DIALOG,
                true,
            ),
        )
    }

    fun updateStrokeColor(strokeColour: Int) {
        preferences.set(key = PreferenceKeys.PREF_SELECTED_STROKE_COLOUR, value = strokeColour)
    }

    fun updateStrokeWidth(strokeWidth: Int) {
        preferences.set(key = PreferenceKeys.PREF_SELECTED_STROKE_WIDTH, value = strokeWidth)
    }

    fun updateStylusOnlySetting(useStylusOnly: Boolean) {
        preferences.set(key = PreferenceKeys.PREF_USE_STYLUS_ONLY, value = useStylusOnly)
    }

    fun updatePressureDetectionSetting(detectPressure: Boolean) {
        preferences.set(key = PreferenceKeys.PREF_DETECT_PRESSURE, value = detectPressure)
    }

    fun updateEnableStylusDialog(canShow: Boolean) {
        preferences.set(key = PreferenceKeys.PREF_SHOW_ENABLE_STYLUS_DIALOG, value = canShow)
    }

    fun updateDisableStylusDialog(canShow: Boolean) {
        preferences.set(key = PreferenceKeys.PREF_SHOW_DISABLE_STYLUS_DIALOG, value = canShow)
    }

}