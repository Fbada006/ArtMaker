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
import android.graphics.Bitmap
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.core.os.BuildCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.artmaker.actions.ArtMakerAction
import com.artmaker.actions.ExportType
import com.artmaker.artmaker.R
import com.artmaker.composables.ArtMakerControlMenu
import com.artmaker.composables.ArtMakerDrawScreen
import com.artmaker.composables.StrokeWidthSlider
import com.artmaker.models.ArtMakerConfiguration
import com.artmaker.viewmodels.ArtMakerViewModel

/**
 * [ArtMaker] composable which has our draw screen and controllers
 * We will expose this composable and test our Library on the app layer
 */
@OptIn(BuildCompat.PrereleaseSdkCheck::class)
@Composable
fun ArtMaker(modifier: Modifier = Modifier, onFinishDrawing: (Bitmap) -> Unit = {}, artMakerConfiguration: ArtMakerConfiguration = ArtMakerConfiguration()) {
    val context = LocalContext.current
    val viewModel: ArtMakerViewModel = viewModel(
        factory = ArtMakerViewModel.provideFactory(application = context.applicationContext as Application),
    )
    var showStrokeWidth by remember { mutableStateOf(value = false) }
    val artMakerUIState by viewModel.artMakerUIState.collectAsStateWithLifecycle()
    val shouldTriggerArtExport by viewModel.shouldTriggerArtExport.collectAsStateWithLifecycle()
    val finishedImage by viewModel.finishedImage.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = finishedImage) {
        finishedImage?.let { onFinishDrawing(it) }
    }

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                visible = viewModel.pathList.isNotEmpty(),
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.Padding60)),
            ) {
                Column {
                    FloatingActionButton(onClick = { viewModel.onAction(ArtMakerAction.TriggerArtExport(ExportType.ShareImage)) }) {
                        Icon(imageVector = Icons.Filled.Share, contentDescription = Icons.Filled.Share.name)
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.Padding4)))
                    FloatingActionButton(onClick = { viewModel.onAction(ArtMakerAction.TriggerArtExport(ExportType.FinishDrawingImage)) }) {
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
                artMakerConfiguration = artMakerConfiguration,
                onDrawEvent = {
                    showStrokeWidth = false
                    viewModel.onDrawEvent(it)
                },
                onAction = viewModel::onAction,
                pathList = viewModel.pathList,
                shouldTriggerArtExport = shouldTriggerArtExport,
                imageBitmap = viewModel.backgroundImage.value,
            )
            StrokeWidthSlider(
                state = artMakerUIState,
                onAction = viewModel::onAction,
                isVisible = showStrokeWidth,
                artMakerConfiguration = artMakerConfiguration,
            )
            ArtMakerControlMenu(
                state = artMakerUIState,
                onAction = viewModel::onAction,
                modifier = Modifier.height(dimensionResource(id = R.dimen.Padding60)),
                onShowStrokeWidthPopup = {
                    showStrokeWidth = !showStrokeWidth
                },
                setBackgroundImage = viewModel::setImage,
                imageBitmap = viewModel.backgroundImage.value,
                artMakerConfiguration = artMakerConfiguration,
            )
        }
    }
}
