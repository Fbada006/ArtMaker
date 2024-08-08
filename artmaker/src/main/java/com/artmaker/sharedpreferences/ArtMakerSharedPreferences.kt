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
package com.artmaker.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.artmaker.artmaker.R

/**
 * This is a SharedPreferences implementation that uses a Generic class to store the user's data...
 */
internal class ArtMakerSharedPreferences(
    context: Context,
) {

    private val artMakerSharedPreferences: SharedPreferences = context.getSharedPreferences(
        "artMakerSharedPreferences",
        Context.MODE_PRIVATE,
    )

    inline fun <reified T> set(key: String, value: T) {
        val editor = artMakerSharedPreferences.edit()
        when (T::class) {
            Boolean::class -> editor.putBoolean(key, value as Boolean)
            Int::class -> editor.putInt(key, value as Int)
            String::class -> editor.putString(key, value as String)
            Float::class -> editor.putFloat(key, value as Float)
            Long::class -> editor.putLong(key, value as Long)
            else -> {
                throw IllegalArgumentException(
                    "Could not save class type of ${T::class} to preferences."
                )
            }
        }
        editor.apply()
    }

    inline fun <reified T> get(key: String, defaultValue: T): T {
        when (T::class) {
            Boolean::class -> return artMakerSharedPreferences.getBoolean(
                key,
                defaultValue as Boolean,
            ) as T

            Int::class -> return artMakerSharedPreferences.getInt(key, defaultValue as Int) as T
            String::class -> return artMakerSharedPreferences.getString(
                key,
                defaultValue as String,
            ) as T

            Float::class -> return artMakerSharedPreferences.getFloat(
                key,
                defaultValue as Float,
            ) as T

            Long::class -> return artMakerSharedPreferences.getLong(key, defaultValue as Long) as T
        }
        return defaultValue
    }
}
