package com.fbada006.shared.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.fbada006.shared.utils.ContextProvider

actual fun getDataStore(): DataStore<Preferences> {
    val context = ContextProvider.getContext()
    return createDataStore {
        context.filesDir.resolve(ARTMAKER_DATASTORE_FILE_NAME).absolutePath
    }
}