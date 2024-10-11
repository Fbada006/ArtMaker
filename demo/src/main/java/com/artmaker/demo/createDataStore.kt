package com.artmaker.demo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.fbada006.shared.data.ARTMAKER_DATASTORE_FILE_NAME
import com.fbada006.shared.data.createDataStore

fun createDataStore(context: Context): DataStore<Preferences> {
    return createDataStore {
        context.filesDir.resolve(ARTMAKER_DATASTORE_FILE_NAME).absolutePath
    }
}