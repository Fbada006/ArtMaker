package com.artmaker.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.artmaker.viewmodels.ArtMakerViewModel

/**
 * [ArtMakerDrawScreen] Composable where we will implement the draw logic.
 * For now, this is just an empty blue screen.
 */
@Composable
internal fun ArtMakerDrawScreen(
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val artMakerViewModel: ArtMakerViewModel = viewModel(
        factory = ArtMakerViewModel.provideFactory(context = context)
    )
    val artMakerUIState by artMakerViewModel.artMakerUIState.collectAsState()

    Canvas(
        modifier = modifier
            .background(color = Color(color = artMakerUIState.backgroundColour))
    ) {

    }
}