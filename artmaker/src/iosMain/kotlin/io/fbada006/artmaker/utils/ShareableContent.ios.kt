/*
 * Copyright 2024 ArtMaker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fbada006.artmaker.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.interop.LocalUIViewController
import io.fbada006.artmaker.actions.ArtMakerAction
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.ImageInfo
import platform.CoreGraphics.CGSizeMake
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun ShareableContent(shouldExport: Boolean, onAction: (ArtMakerAction) -> Unit, content: @Composable () -> Unit, modifier: Modifier) {
    val uiViewController = LocalUIViewController.current

    LaunchedEffect(shouldExport) {
        if (shouldExport) {
            val view = uiViewController.view
            val bounds = view.bounds
            val size = CGSizeMake(
                width = bounds.useContents { size.width },
                height = bounds.useContents { size.height },
            )

            UIGraphicsBeginImageContextWithOptions(size, false, 0.0)

            view.drawViewHierarchyInRect(bounds, afterScreenUpdates = true)

            val screenshot = UIGraphicsGetImageFromCurrentImageContext()
            UIGraphicsEndImageContext()

            val imageBitmap = screenshot?.let { uiImage ->
                try {
                    val width = uiImage.size.useContents { width }.toInt()
                    val height = uiImage.size.useContents { height }.toInt()

                    val bitmap = Bitmap()
                    val info = ImageInfo(
                        width = width,
                        height = height,
                        colorType = ColorType.RGBA_8888,
                        alphaType = ColorAlphaType.PREMUL,
                    )

                    bitmap.setImageInfo(info)
                    bitmap.allocPixels()

                    bitmap.asComposeImageBitmap()
                } catch (e: Exception) {
                    println("Error creating bitmap: ${e.message}")
                    null
                }
            }

            onAction(ArtMakerAction.ExportArt(imageBitmap))
        }
    }

    Box(modifier = modifier) {
        content()
    }
}
