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
package com.fbada006.shared.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.fbada006.shared.ArtMakerUIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * PreferencesManager is used to handle the [ArtMakerSharedPreferences] functionalities and abstract them from [ArtMakerViewModel].
 */
internal class PreferencesManager(private val preferences: DataStore<Preferences>) {

//    fun loadInitialUIState(): ArtMakerUIState = ArtMakerUIState(
//        strokeColour = preferences.get(
//            key = PreferenceKeys.PREF_SELECTED_STROKE_COLOUR,
//            defaultValue = Color.Red.toArgb(),
//        ),
//        strokeWidth = preferences.get(
//            key = PreferenceKeys.PREF_SELECTED_STROKE_WIDTH,
//            defaultValue = 5,
//        ),
//        shouldUseStylusOnly = preferences.get(
//            key = PreferenceKeys.PREF_USE_STYLUS_ONLY,
//            defaultValue = false,
//        ),
//        shouldDetectPressure = preferences.get(
//            key = PreferenceKeys.PREF_DETECT_PRESSURE,
//            defaultValue = false,
//        ),
//        canShowEnableStylusDialog = preferences.get(
//            key = PreferenceKeys.PREF_SHOW_ENABLE_STYLUS_DIALOG,
//            defaultValue = true,
//        ),
//        canShowDisableStylusDialog = preferences.get(
//            key = PreferenceKeys.PREF_SHOW_DISABLE_STYLUS_DIALOG,
//            defaultValue = true,
//        ),
//    )

    suspend fun updateStrokeColor(strokeColour: Int) {
        preferences.edit { datastore ->
            val keys = intPreferencesKey(PreferenceKeys.PREF_SELECTED_STROKE_COLOUR)
            datastore[keys] = strokeColour
        }
    }

    suspend fun updateStrokeWidth(strokeWidth: Int) {
        preferences.edit { datastore ->
            val keys = intPreferencesKey(PreferenceKeys.PREF_SELECTED_STROKE_WIDTH)
            datastore[keys] = strokeWidth
        }
    }

    suspend fun updateStylusOnlySetting(useStylusOnly: Boolean) {
        preferences.edit { datastore ->
            val keys = booleanPreferencesKey(PreferenceKeys.PREF_USE_STYLUS_ONLY)
            datastore[keys] = useStylusOnly
        }
    }

    suspend fun updatePressureDetectionSetting(detectPressure: Boolean) {
        preferences.edit { datastore ->
            val keys = booleanPreferencesKey(PreferenceKeys.PREF_DETECT_PRESSURE)
            datastore[keys] = detectPressure
        }
    }

    suspend fun updateEnableStylusDialog(canShow: Boolean) {
        preferences.edit { datastore ->
            val keys = booleanPreferencesKey(PreferenceKeys.PREF_SHOW_ENABLE_STYLUS_DIALOG)
            datastore[keys] = canShow
        }
    }

    suspend fun updateDisableStylusDialog(canShow: Boolean) {
        preferences.edit { datastore ->
            val keys = booleanPreferencesKey(PreferenceKeys.PREF_SHOW_DISABLE_STYLUS_DIALOG)
            datastore[keys] = canShow
        }
    }

    fun getStrokeColor(): Flow<Int> =
        preferences.data.map { preferences ->
            val counterKey = intPreferencesKey(PreferenceKeys.PREF_SELECTED_STROKE_COLOUR)
            preferences[counterKey] ?: Color.Red.toArgb()
        }

    fun getStrokeWidth(): Flow<Int> =
        preferences.data.map { preferences ->
            val counterKey = intPreferencesKey(PreferenceKeys.PREF_SELECTED_STROKE_WIDTH)
            preferences[counterKey] ?: 5
        }

    fun getStylusOnlySetting(): Flow<Boolean> =
        preferences.data.map { preferences ->
            val counterKey = booleanPreferencesKey(PreferenceKeys.PREF_USE_STYLUS_ONLY)
            preferences[counterKey] ?: false
        }

    fun getPressureDetectionSetting(): Flow<Boolean> =
        preferences.data.map { preferences ->
            val counterKey = booleanPreferencesKey(PreferenceKeys.PREF_DETECT_PRESSURE)
            preferences[counterKey] ?: false
        }

    fun getEnableStylusDialog(): Flow<Boolean> =
        preferences.data.map { preferences ->
            val counterKey = booleanPreferencesKey(PreferenceKeys.PREF_SHOW_ENABLE_STYLUS_DIALOG)
            preferences[counterKey] ?: true
        }

    fun getDisableStylusDialog(): Flow<Boolean> =
        preferences.data.map { preferences ->
            val counterKey = booleanPreferencesKey(PreferenceKeys.PREF_SHOW_DISABLE_STYLUS_DIALOG)
            preferences[counterKey] ?: true
        }
}
