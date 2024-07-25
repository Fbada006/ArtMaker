package com.artmaker.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

/**
 * This is a SharedPreferences implementation that uses a Generic class to store the user's data...
 */

internal class ArtMakerSharedPreferences<T>(
    context: Context
) {

    private val artMakerSharedPreferences = context.getSharedPreferences(
        "artMakerSharedPreferences",
        Context.MODE_PRIVATE
    )

    inline fun <reified T> SharedPreferences.set(key: String, value: T) {
        val editor = this.edit()
        when (T::class) {
            Boolean::class -> editor.putBoolean(key, value as Boolean)
            Int::class -> editor.putInt(key, value as Int)
            String::class -> editor.putString(key, value as String)
            Float::class -> editor.putFloat(key, value as Float)
            Long::class -> editor.putLong(key, value as Long)
            else -> {
                val otherValue = value.toString()
                artMakerSharedPreferences.edit().putString(key, otherValue).apply()
            }
        }
        editor.apply()
    }

    inline fun <reified T> SharedPreferences.get(key: String, defaultValue: T): T {
        when(T::class) {
            Boolean::class -> return this.getBoolean(key, defaultValue as Boolean) as T
            Int::class -> return this.getInt(key, defaultValue as Int) as T
            String::class -> return this.getString(key, defaultValue as String) as T
            Float::class -> return this.getFloat(key, defaultValue as Float) as T
            Long::class -> return this.getLong(key, defaultValue as Long) as T
        }
        return defaultValue
    }

}