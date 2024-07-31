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
package com.artmaker

import androidx.compose.foundation.layout.Column
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
fun ArtMaker(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        ArtMakerDrawScreen(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
        )
        ArtMakerControlMenu(
            onStrokeWidthActionClicked = {},
            onUnDoActionClicked = {},
            onRedoActionClicked = {},
            onClearActionClicked = {},
            onColorSelected = {},
            onUpdateBackgroundActionClicked = {},
            onExportFileActionClicked = {},
            modifier = Modifier.height(60.dp),
        )
    }
}
