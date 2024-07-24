package com.artmaker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artmaker.composables.ArtMakerControlMenu
import com.artmaker.composables.ArtMakerDrawScreen

/**
 * [ArtMaker] composable which has our draw screen and controllers
 * We will expose this composable and test our Library on the app layer
 */
@Composable
fun ArtMaker() {
  Column {
    ArtMakerDrawScreen(
      modifier = Modifier
        .fillMaxSize()
        .weight(1f, fill = false),
    )
    Spacer(modifier = Modifier.height(10.dp))
    ArtMakerControlMenu(
      onExportArt = {},
      onUnDoActionClicked = {},
      onRedoActionClicked = {},
      onClearActionClicked = {},
      onUpdateBackgroundActionClicked = {},
      onColorSelected = {}
    )
    Spacer(modifier = Modifier.height(40.dp))
  }
}