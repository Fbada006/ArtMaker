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
package io.fbada006.artmaker.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.fbada006.artmaker.data.PreferenceKeys.PREF_CUSTOM_COLORS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class CustomColors(val colors: List<Int>)

private const val MAX_CUSTOM_COLORS = 5 // Similar to num columns in the color picker. We just want one row

internal class CustomColorsManager(private val preferences: DataStore<Preferences> = getDataStore) {
    private val json = Json { ignoreUnknownKeys = true }

    // Saves the color and ensures the list of items is a maximum of MAX_CUSTOM_COLORS
    suspend fun saveColor(color: Int) {
        val currentColors = getColors().firstOrNull() ?: emptyList()
        var colors = currentColors.toMutableList()
        colors.add(color)
        if (colors.size > MAX_CUSTOM_COLORS) {
            colors = colors.takeLast(MAX_CUSTOM_COLORS).toMutableList()
        }

        val customColors = CustomColors(colors)
        val jsonStr = json.encodeToString(serializer = CustomColors.serializer(), value = customColors)
        preferences.edit { datastore ->
            val keys = stringPreferencesKey(PREF_CUSTOM_COLORS)
            datastore[keys] = jsonStr
        }
    }

    // Returns all the custom colours saved. Should always be equal to MAX_CUSTOM_COLORS
    fun getColors(): Flow<List<Int>> = flow {
        val customColorsFromDatastore = preferences.data.map { datastore ->
            val counterKey = stringPreferencesKey(PREF_CUSTOM_COLORS)
            datastore[counterKey] ?: ""
        }
        customColorsFromDatastore.collect { colors ->
            if (colors.isNotEmpty()) {
                val serializableList = json.decodeFromString<CustomColors>(colors)
                val colorList = serializableList.colors.distinct()
                emit(colorList)
            } else {
                emit(emptyList())
            }
        }
    }
}
