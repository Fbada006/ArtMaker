package io.fbada006.artmaker.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
actual fun getScreenSize(): Size {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val height = with(density) { configuration.screenHeightDp.dp.toPx() }
    val width = with(density) { configuration.screenWidthDp.dp.toPx() }

    return Size(width = width, height = height)
}