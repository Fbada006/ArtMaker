package com.fbada006.shared.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.fbada006.shared.actions.ArtMakerAction

@Composable
actual fun ShareableContent(shouldExport:Boolean,onAction: (ArtMakerAction) -> Unit, content: @Composable () -> Unit, modifier: Modifier) {
}