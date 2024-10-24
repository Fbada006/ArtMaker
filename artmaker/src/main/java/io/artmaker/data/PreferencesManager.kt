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
import io.artmaker.composables.LineStyle

/**
 * PreferencesManager is used to handle the [ArtMakerSharedPreferences] functionalities and abstract them from [ArtMakerViewModel].
 */
internal class PreferencesManager(private val preferences: ArtMakerSharedPreferences) {

    fun loadInitialUIState(): ArtMakerUIState = ArtMakerUIState(
        strokeColour = getStrokeColor(),
        strokeWidth = getStrokeWidth(),
        shouldUseStylusOnly = getStylusOnlySetting(),
        shouldDetectPressure = getPressureDetectionSetting(),
        canShowEnableStylusDialog = getEnableStylusDialog(),
        canShowDisableStylusDialog = getDisableStylusDialog(),
        lineStyle = getLineStyle(),
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

    fun updateLineStyle(style: LineStyle) {
        preferences.set(key = PreferenceKeys.PREF_LINE_STYLE, value = style.toString())
    }

    fun getLineStyle(): LineStyle {
        val style = preferences.get(
            key = PreferenceKeys.PREF_LINE_STYLE,
            defaultValue = LineStyle.FILLED.toString(),
        )

        return LineStyle.valueOf(style)
    }

    fun getStrokeColor(): Int = preferences.get(
        key = PreferenceKeys.PREF_SELECTED_STROKE_COLOUR,
        defaultValue = Color.Red.toArgb(),
    )

    fun getStrokeWidth(): Int = preferences.get(
        key = PreferenceKeys.PREF_SELECTED_STROKE_WIDTH,
        defaultValue = 5,
    )

    fun getStylusOnlySetting(): Boolean = preferences.get(
        key = PreferenceKeys.PREF_USE_STYLUS_ONLY,
        defaultValue = false,
    )

    fun getPressureDetectionSetting(): Boolean = preferences.get(
        key = PreferenceKeys.PREF_DETECT_PRESSURE,
        defaultValue = false,
    )

    fun getEnableStylusDialog(): Boolean = preferences.get(
        key = PreferenceKeys.PREF_SHOW_ENABLE_STYLUS_DIALOG,
        defaultValue = true,
    )

    fun getDisableStylusDialog(): Boolean = preferences.get(
        key = PreferenceKeys.PREF_SHOW_DISABLE_STYLUS_DIALOG,
        defaultValue = true,
    )
}
