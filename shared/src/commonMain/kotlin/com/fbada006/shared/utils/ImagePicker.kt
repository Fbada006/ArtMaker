package com.fbada006.shared.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

expect class ImagePicker {

    @Composable
    fun registerPicker(onImagePicked: (ImageBitmap) -> Unit)

    fun pickImage()
}

