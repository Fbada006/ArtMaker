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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

/**
 * PreferencesManager is used to handle the  functionalities and abstract them from [com.fbada006.shared.ArtMakerViewModel].
 */
internal class PreferencesManager(private val preferences: DataStore<Preferences> = getDataStore()) {

    val state = combine(
        getStrokeColor(),
        getStrokeWidth(),
        getStylusOnlySetting(),
        getPressureDetectionSetting(),
        getEnableStylusDialog(),
        getDisableStylusDialog(),
    ) { values->
        ArtMakerUIState(
            strokeColour = values[0] as Int,
            strokeWidth = values[1] as Int,
            shouldUseStylusOnly = values[2] as Boolean,
            shouldDetectPressure = values[3] as Boolean,
            canShowEnableStylusDialog = values[4] as Boolean,
            canShowDisableStylusDialog = values[5] as Boolean,
        )
    }

    suspend fun updateStrokeColor(strokeColour: Int) {
        preferences.edit { datastore ->
            val key = intPreferencesKey(PreferenceKeys.PREF_SELECTED_STROKE_COLOUR)
            datastore[key] = strokeColour
        }
    }

    suspend fun updateStrokeWidth(strokeWidth: Int) {
        preferences.edit { datastore ->
            val key = intPreferencesKey(PreferenceKeys.PREF_SELECTED_STROKE_WIDTH)
            datastore[key] = strokeWidth
        }
    }

    suspend fun updateStylusOnlySetting(useStylusOnly: Boolean) {
        preferences.edit { datastore ->
            val key = booleanPreferencesKey(PreferenceKeys.PREF_USE_STYLUS_ONLY)
            datastore[key] = useStylusOnly
        }
    }

    suspend fun updatePressureDetectionSetting(detectPressure: Boolean) {
        preferences.edit { datastore ->
            val key = booleanPreferencesKey(PreferenceKeys.PREF_DETECT_PRESSURE)
            datastore[key] = detectPressure
        }
    }

    suspend fun updateEnableStylusDialog(canShow: Boolean) {
        preferences.edit { datastore ->
            val key = booleanPreferencesKey(PreferenceKeys.PREF_SHOW_ENABLE_STYLUS_DIALOG)
            datastore[key] = canShow
        }
    }

    suspend fun updateDisableStylusDialog(canShow: Boolean) {
        preferences.edit { datastore ->
            val key = booleanPreferencesKey(PreferenceKeys.PREF_SHOW_DISABLE_STYLUS_DIALOG)
            datastore[key] = canShow
        }
    }

    private fun getStrokeColor(): Flow<Int> =
        preferences.data.map { preferences ->
            val key = intPreferencesKey(PreferenceKeys.PREF_SELECTED_STROKE_COLOUR)
            preferences[key] ?: Color.Red.toArgb()
        }

    private fun getStrokeWidth(): Flow<Int> =
        preferences.data.map { preferences ->
            val key = intPreferencesKey(PreferenceKeys.PREF_SELECTED_STROKE_WIDTH)
            preferences[key] ?: 5
        }

    private fun getStylusOnlySetting(): Flow<Boolean> =
        preferences.data.map { preferences ->
            val key = booleanPreferencesKey(PreferenceKeys.PREF_USE_STYLUS_ONLY)
            preferences[key] ?: false
        }

    private fun getPressureDetectionSetting(): Flow<Boolean> =
        preferences.data.map { preferences ->
            val key = booleanPreferencesKey(PreferenceKeys.PREF_DETECT_PRESSURE)
            preferences[key] ?: false
        }

    private fun getEnableStylusDialog(): Flow<Boolean> =
        preferences.data.map { preferences ->
            val key = booleanPreferencesKey(PreferenceKeys.PREF_SHOW_ENABLE_STYLUS_DIALOG)
            preferences[key] ?: true
        }

    private fun getDisableStylusDialog(): Flow<Boolean> =
        preferences.data.map { preferences ->
            val key = booleanPreferencesKey(PreferenceKeys.PREF_SHOW_DISABLE_STYLUS_DIALOG)
            preferences[key] ?: true
        }
}
