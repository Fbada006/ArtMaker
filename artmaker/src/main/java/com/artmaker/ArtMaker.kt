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
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.os.BuildCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.artmaker.actions.ArtMakerAction
import com.artmaker.composables.ArtMakerControlMenu
import com.artmaker.composables.ArtMakerDrawScreen
import com.artmaker.composables.CONTROL_MENU_HEIGHT
import com.artmaker.composables.StrokeWidthSlider
import com.artmaker.viewmodels.ArtMakerViewModel

/**
 * [ArtMaker] composable which has our draw screen and controllers
 * We will expose this composable and test our Library on the app layer
 */
@OptIn(BuildCompat.PrereleaseSdkCheck::class)
@Composable
fun ArtMaker(modifier: Modifier = Modifier, onFinishDrawing: (Uri?) -> Unit = {}) {
    val context = LocalContext.current
    val viewModel: ArtMakerViewModel = viewModel(
        factory = ArtMakerViewModel.provideFactory(application = context.applicationContext as Application),
    )
    var showStrokeWidth by remember { mutableStateOf(value = false) }
    val artMakerUIState by viewModel.artMakerUIState.collectAsStateWithLifecycle()
    val shouldTriggerArtExport by viewModel.shouldTriggerArtExport.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(visible = viewModel.pathList.isNotEmpty()) {
                Column(modifier = Modifier.padding(bottom = CONTROL_MENU_HEIGHT)) {
                    FloatingActionButton(onClick = { viewModel.onAction(ArtMakerAction.TriggerArtExport) }) {
                        Icon(imageVector = Icons.Filled.Share, contentDescription = Icons.Filled.Share.name)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    FloatingActionButton(onClick = { }) {
                        Icon(imageVector = Icons.Filled.Done, contentDescription = Icons.Filled.Done.name)
                    }
                }
            }
        },
    ) { values ->
        Column(modifier = modifier.padding(values)) {
            ArtMakerDrawScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                state = artMakerUIState,
                onDrawEvent = {
                    showStrokeWidth = false
                    viewModel.onDrawEvent(it)
                },
                onAction = viewModel::onAction,
                pathList = viewModel.pathList,
                shouldTriggerArtExport = shouldTriggerArtExport,
                imageBitmap = viewModel.imageBitmap.value,
            )
            StrokeWidthSlider(
                state = artMakerUIState,
                onAction = viewModel::onAction,
                isVisible = showStrokeWidth,
            )
            ArtMakerControlMenu(
                state = artMakerUIState,
                onAction = viewModel::onAction,
                modifier = Modifier.height(CONTROL_MENU_HEIGHT),
                onShowStrokeWidthPopup = {
                    showStrokeWidth = !showStrokeWidth
                },
                setBackgroundImage = viewModel::setImage,
                imageBitmap = viewModel.imageBitmap.value,
            )
        }
        Column(modifier = modifier) {
            ArtMakerDrawScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                state = artMakerUIState,
                onDrawEvent = {
                    showStrokeWidth = false
                    viewModel.onDrawEvent(it)
                },
                onAction = viewModel::onAction,
                pathList = viewModel.pathList,
                shouldTriggerArtExport = shouldTriggerArtExport,
                imageBitmap = viewModel.imageBitmap.value,
            )
            StrokeWidthSlider(
                state = artMakerUIState,
                onAction = viewModel::onAction,
                isVisible = showStrokeWidth,
            )
            ArtMakerControlMenu(
                state = artMakerUIState,
                onAction = viewModel::onAction,
                modifier = Modifier.height(CONTROL_MENU_HEIGHT),
                onShowStrokeWidthPopup = {
                    showStrokeWidth = !showStrokeWidth
                },
                setBackgroundImage = viewModel::setImage,
                imageBitmap = viewModel.imageBitmap.value,
            )
        }
    }
}
