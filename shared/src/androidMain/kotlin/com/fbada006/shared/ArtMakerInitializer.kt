package com.fbada006.shared

import android.content.Context
import com.fbada006.shared.utils.ContextProvider

/**
 * Has to be called with a valid context from Android
 */
object ArtMakerInitializer {

    fun initialise(context: Context) {
        ContextProvider.setContext(context)
    }
}