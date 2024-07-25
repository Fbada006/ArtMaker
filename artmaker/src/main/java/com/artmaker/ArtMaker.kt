package com.artmaker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.artmaker.composables.ArtMakerControlMenu
import com.artmaker.composables.ArtMakerDrawScreen

/**
 * [ArtMaker] composable which has our draw screen and controllers
 * We will expose this composable and test our Library on the app layer
 */
@Composable
fun ArtMaker(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        ArtMakerDrawScreen(
            modifier = Modifier
              .fillMaxSize()
              .weight(1f),
        )
        ArtMakerControlMenu(
            onExportArt = {},
            onUnDoActionClicked = {},
            onRedoActionClicked = {},
            onClearActionClicked = {},
            onUpdateBackgroundActionClicked = {},
            onColorSelected = {}
        )
    }
}