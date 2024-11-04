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

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIApplication
import platform.UIKit.UIScreen

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun getScreenSize(): Size {
    val screen = UIScreen.mainScreen
    val screenRect = screen.bounds
    val safeAreaInsets = UIApplication.sharedApplication.keyWindow?.safeAreaInsets
    val screenScale = screen.scale

    val verticalInsets = safeAreaInsets?.useContents {
        top + bottom
    } ?: 0.0

    val screenWidth = screenRect.useContents {
        size.width
    }

    val screenHeight = screenRect.useContents {
        size.height - verticalInsets
    }

    return screenRect.useContents {
        Size(width = (screenWidth * screenScale).toFloat(), height = (screenHeight * screenScale).toFloat())
    }
}