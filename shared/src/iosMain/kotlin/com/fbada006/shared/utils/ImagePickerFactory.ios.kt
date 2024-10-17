package com.fbada006.shared.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIViewController

actual class ImagePickerFactory(
    private val rootController: UIViewController
){

    @Composable
    actual fun createPicker(): ImagePicker {
        return remember {
            ImagePicker(rootController)
        }
    }
}