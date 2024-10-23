package com.fbada006.shared.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.interop.LocalUIViewController
import platform.UIKit.UIViewController


@Composable
actual fun createPicker(): ImagePicker {
    val rootController = LocalUIViewController.current
    return remember {
        ImagePicker(rootController)
    }
}