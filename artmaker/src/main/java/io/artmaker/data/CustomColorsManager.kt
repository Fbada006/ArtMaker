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

import android.content.Context
import io.artmaker.data.PreferenceKeys.PREF_CUSTOM_COLORS
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class CustomColors(val colors: List<Int>)

private const val MAX_CUSTOM_COLORS = 5 // Similar to num columns in the color picker. We just want one row

internal class CustomColorsManager(context: Context) {
    private val json = Json { ignoreUnknownKeys = true }
    private val preferences = ArtMakerSharedPreferences(context)

    // Saves the color and ensures the list of items is a maximum of MAX_CUSTOM_COLORS
    fun saveColor(color: Int) {
        var colors = getColors().toMutableList()
        colors.add(color)

        if (colors.size > MAX_CUSTOM_COLORS) {
            colors = colors.takeLast(MAX_CUSTOM_COLORS).toMutableList()
        }

        val customColors = CustomColors(colors)
        val jsonStr = json.encodeToString(serializer = CustomColors.serializer(), value = customColors)
        preferences.set(PREF_CUSTOM_COLORS, jsonStr)
    }

    // Returns all the custom colours saved. Should always be equal to MAX_CUSTOM_COLORS
    fun getColors(): List<Int> {
        val json = preferences.get(PREF_CUSTOM_COLORS, "")
        return if (json.isNotEmpty()) {
            val serializableList = this.json.decodeFromString<CustomColors>(json)
            serializableList.colors.distinct()
        } else {
            emptyList()
        }
    }
}
