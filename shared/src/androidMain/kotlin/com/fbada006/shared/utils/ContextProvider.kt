package com.fbada006.shared.utils

import android.content.Context

/**
 * We know what we are doing holding this context here (Hopefully)
 */
object ContextProvider {

    private var applicationContext: Context? = null

    fun setContext(context: Context) {
        applicationContext = context
    }

    fun getContext(): Context {
        return checkNotNull(applicationContext) { "The application context should not be null. Please call fun setApplicationContext(context: Context) first" }
    }
}