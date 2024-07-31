package com.artmaker.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

/**
 * This is a SharedPreferences implementation that uses a Generic class to store the user's data...
 */
class ArtMakerSharedPreferences(
    context: Context
) {

    val artMakerSharedPreferences: SharedPreferences = context.getSharedPreferences(
        "artMakerSharedPreferences",
        Context.MODE_PRIVATE
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
                throw IllegalArgumentException("Could not save class type of ${T::class} to SharedPreferences...")
            }
        }
        editor.apply()
    }

    inline fun <reified T> get(key: String, defaultValue: T): T {
        when (T::class) {
            Boolean::class -> return artMakerSharedPreferences.getBoolean(
                key,
                defaultValue as Boolean
            ) as T

            Int::class -> return artMakerSharedPreferences.getInt(key, defaultValue as Int) as T
            String::class -> return artMakerSharedPreferences.getString(
                key,
                defaultValue as String
            ) as T

            Float::class -> return artMakerSharedPreferences.getFloat(
                key,
                defaultValue as Float
            ) as T

            Long::class -> return artMakerSharedPreferences.getLong(key, defaultValue as Long) as T
        }
        return defaultValue
    }

}