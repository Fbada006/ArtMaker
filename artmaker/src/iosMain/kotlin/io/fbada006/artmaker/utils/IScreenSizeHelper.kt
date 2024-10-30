package io.fbada006.artmaker.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalWindowInfo

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenSize(): Size {
    val size = LocalWindowInfo.current.containerSize
    return Size(width = size.width.toFloat(), height = size.height.toFloat())
}