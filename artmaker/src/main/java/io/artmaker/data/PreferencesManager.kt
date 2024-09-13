/*
 * Copyright 2024 ArtMaker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.artmaker.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import io.artmaker.ArtMakerUIState

/**
 * PreferencesManager is used to handle the [ArtMakerSharedPreferences] functionalities and abstract them from [ArtMakerViewModel].
 */

internal class PreferencesManager(private val preferences: ArtMakerSharedPreferences) {

    fun loadInitialUIState(): ArtMakerUIState = ArtMakerUIState(
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
            defaultValue = false,
        ),
        shouldDetectPressure = preferences.get(
            key = PreferenceKeys.PREF_DETECT_PRESSURE,
            defaultValue = false,
        ),
        canShowEnableStylusDialog = preferences.get(
            key = PreferenceKeys.PREF_SHOW_ENABLE_STYLUS_DIALOG,
            defaultValue = true,
        ),
        canShowDisableStylusDialog = preferences.get(
            key = PreferenceKeys.PREF_SHOW_DISABLE_STYLUS_DIALOG,
            defaultValue = true,
        ),
    )

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
