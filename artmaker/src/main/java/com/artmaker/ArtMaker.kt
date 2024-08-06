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

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.artmaker.composables.ArtMakerControlMenu
import com.artmaker.composables.ArtMakerDrawScreen
import com.artmaker.composables.CONTROL_MENU_HEIGHT
import com.artmaker.viewmodels.ArtMakerViewModel

/**
 * [ArtMaker] composable which has our draw screen and controllers
 * We will expose this composable and test our Library on the app layer
 */
@Composable
fun ArtMaker(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel: ArtMakerViewModel = viewModel(
        factory = ArtMakerViewModel.provideFactory(application = context.applicationContext as Application),
    )
    val artMakerUIState by viewModel.artMakerUIState.collectAsStateWithLifecycle()
    val shouldTriggerArtExport by viewModel.shouldTriggerArtExport.collectAsStateWithLifecycle()

    Column(modifier = modifier) {
        ArtMakerDrawScreen(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            state = artMakerUIState,
            onDrawEvent = viewModel::onDrawEvent,
            onAction = viewModel::onAction,
            pathList = viewModel.pathList,
            shouldTriggerArtExport = shouldTriggerArtExport,
        )
        ArtMakerControlMenu(
            onAction = viewModel::onAction,
            state = artMakerUIState,
            modifier = Modifier.height(CONTROL_MENU_HEIGHT),
        )
    }
}
