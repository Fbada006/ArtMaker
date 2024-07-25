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
        "artMakerSharedPrefernces",
        Context.MODE_PRIVATE
    )

    //private val editor = artMakerSharedPreferences.edit()

    inline fun <reified T> SharedPreferences.set(key: String, value: T): T {
        val editor = this.edit()
        when (T::class) {
            Boolean::class -> editor.putBoolean(key, value as Boolean)
        }
        editor.apply()
    }

    inline fun <reified T> SharedPreferences.get(key: String, value: T): T {
        when(T::class) {
            Boolean::class -> return this.getBoolean(key, value as Boolean) as T
        }
        return value
    }

}