package com.artmaker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.artmaker.composables.ArtMakerControlMenu
import com.artmaker.composables.ArtMakerDrawScreen
import com.artmaker.viewmodels.ArtMakerViewModel

/**
 * [ArtMaker] composable which has our draw screen and controllers
 */
@Composable
fun ArtMaker(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val artMakerViewModel: ArtMakerViewModel = viewModel(
        factory = ArtMakerViewModel.provideFactory(context = context)
    )
    val artMakerUIState by artMakerViewModel.artMakerUIState.collectAsStateWithLifecycle()

    Column(modifier = modifier) {
        ArtMakerDrawScreen(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            state = artMakerUIState
        )
        ArtMakerControlMenu(
            onAction = artMakerViewModel::onAction,
            state = artMakerUIState,
            modifier = Modifier.height(60.dp),
        )
    }
}