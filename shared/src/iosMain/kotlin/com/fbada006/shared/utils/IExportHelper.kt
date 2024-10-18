package com.fbada006.shared.utils

import androidx.compose.ui.graphics.ImageBitmap
import platform.UIKit.*
import platform.UIKit.UIActivityViewController

lateinit var triggerIosSharing: () -> Unit

actual suspend fun shareImage(imageBitmap: ImageBitmap?) {
//    val uiImage = arrayOf(imageBitmap)
//    val activityItems = listOf(uiImage)
//    val activityController = UIActivityViewController(activityItems = activityItems, applicationActivities = null )
//
//    UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
//        activityController, true, null,
//    )
    triggerIosSharing()
}