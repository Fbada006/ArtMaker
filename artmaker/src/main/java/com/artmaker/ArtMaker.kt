package com.artmaker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.artmaker.actions.ArtMakerActions
import com.artmaker.composables.ArtMakerControlMenu
import com.artmaker.composables.ArtMakerDrawScreen
import com.artmaker.controllers.ArtMakerController

/**
 * [ArtMaker] composable which has our draw screen and controllers
 * We will expose this composable and test our Library on the app layer
 */
@Composable
fun ArtMaker(artMakerController: ArtMakerController = viewModel()) {

    Column {
        ArtMakerDrawScreen(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f, fill = false),
        )
        ArtMakerControlMenu(
            onAction = artMakerController::onAction
        )
    }
}