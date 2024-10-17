package com.fbada006.shared

import androidx.compose.ui.interop.LocalUIViewController
import androidx.compose.ui.window.ComposeUIViewController
import com.fbada006.shared.utils.ImagePickerFactory

fun mainViewController() = ComposeUIViewController {
    ArtMaker(
        imagePicker = ImagePickerFactory(LocalUIViewController.current).createPicker()
    )
}