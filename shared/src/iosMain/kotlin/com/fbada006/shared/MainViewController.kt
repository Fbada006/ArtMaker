package com.fbada006.shared

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.fbada006.shared.data.createDataStore

fun mainViewController() = ComposeUIViewController {
    ArtMaker(
        preferences = remember { createDataStore() }
    )
}