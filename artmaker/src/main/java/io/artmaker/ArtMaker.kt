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
package io.artmaker

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
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
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
import io.artmaker.actions.ArtMakerAction
import io.artmaker.actions.ExportType
import io.artmaker.composables.ArtMakerControlMenu
import io.artmaker.composables.ArtMakerDrawScreen
import io.artmaker.composables.StrokeSettings
import io.artmaker.models.ArtMakerConfiguration
import io.fbada006.artmaker.R

/**
 * [ArtMaker] has the draw screen as well as control menu (the bar offering customisation options). By default, it is a white screen that allows the user
 * to draw any shape and customise the attributes of the drawing.
 *
 * @param modifier is the [Modifier]
 * @param onFinishDrawing is the callback exposed once the user clicks done (the [FloatingActionButton] with the checkmark) to trigger finish drawing
 * @param artMakerConfiguration is the configuration to customise the appearance of the control menu and other values
 */
@OptIn(BuildCompat.PrereleaseSdkCheck::class)
@Composable
fun ArtMaker(
    modifier: Modifier = Modifier,
    onFinishDrawing: (Bitmap) -> Unit = {},
    artMakerConfiguration: ArtMakerConfiguration = ArtMakerConfiguration(),
) {
    val context = LocalContext.current
    val viewModel: ArtMakerViewModel = viewModel(
        factory = ArtMakerViewModel.provideFactory(application = context.applicationContext as Application),
    )
    var showStrokeSettings by remember { mutableStateOf(value = false) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val shouldTriggerArtExport by viewModel.shouldTriggerArtExport.collectAsStateWithLifecycle()
    val finishedImage by viewModel.finishedImage.collectAsStateWithLifecycle()
    var isFullScreenEnabled by remember { mutableStateOf(false) }
    var isEraserActive by remember { mutableStateOf(value = false) }
    LaunchedEffect(key1 = finishedImage) {
        finishedImage?.let { onFinishDrawing(it) }
    }

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                visible = viewModel.pathList.isNotEmpty() && !showStrokeSettings,
                modifier = Modifier.padding(bottom = if (isFullScreenEnabled) dimensionResource(id = R.dimen.Padding0) else dimensionResource(id = R.dimen.Padding60)),
            ) {
                Column {
                    // Trigger sharing the image. It has to save the image first
                    if (artMakerConfiguration.canShareArt) {
                        FloatingActionButton(onClick = { viewModel.onAction(ArtMakerAction.TriggerArtExport(ExportType.ShareImage)) }) {
                            Icon(imageVector = Icons.Filled.Share, contentDescription = Icons.Filled.Share.name)
                        }
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.Padding4)))
                    // Finish the drawing and hand it back to the calling application as a bitmap
                    FloatingActionButton(onClick = { viewModel.onAction(ArtMakerAction.TriggerArtExport(ExportType.FinishDrawingImage)) }) {
                        Icon(imageVector = Icons.Filled.Done, contentDescription = Icons.Filled.Done.name)
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.Padding4)))
                    FloatingActionButton(onClick = { isFullScreenEnabled = !isFullScreenEnabled }) {
                        Icon(
                            imageVector = if (isFullScreenEnabled) Icons.Filled.Fullscreen else Icons.Filled.FullscreenExit,
                            contentDescription = if (isFullScreenEnabled) Icons.Filled.Fullscreen.name else Icons.Filled.FullscreenExit.name,
                        )
                    }
                }
            }
        },
        modifier = modifier,
    ) { values ->
        Column(modifier = Modifier.padding(values)) {
            ArtMakerDrawScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                artMakerConfiguration = artMakerConfiguration,
                onDrawEvent = {
                    showStrokeSettings = false
                    viewModel.onDrawEvent(it)
                },
                onAction = viewModel::onAction,
                state = DrawState(
                    pathList = viewModel.pathList,
                    shouldTriggerArtExport = shouldTriggerArtExport,
                    backgroundImage = viewModel.backgroundImage.value,
                    isFullScreenMode = isFullScreenEnabled,
                    shouldUseStylusOnly = state.shouldUseStylusOnly,
                    canShowEnableStylusDialog = state.canShowEnableStylusDialog,
                    canShowDisableStylusDialog = state.canShowDisableStylusDialog,
                ),
                isEraserActive = isEraserActive,
                eraserRadius = state.strokeWidth.toFloat(),
            )
            AnimatedVisibility(visible = showStrokeSettings) {
                StrokeSettings(
                    strokeWidth = state.strokeWidth,
                    onAction = viewModel::onAction,
                    configuration = artMakerConfiguration,
                    modifier = Modifier
                        .padding(top = dimensionResource(id = R.dimen.Padding8), end = dimensionResource(id = R.dimen.Padding12), start = dimensionResource(id = R.dimen.Padding12)),
                    shouldUseStylusOnly = state.shouldUseStylusOnly,
                )
            }
            AnimatedVisibility(visible = !isFullScreenEnabled) {
                ArtMakerControlMenu(
                    state = state,
                    onAction = viewModel::onAction,
                    onDrawEvent = viewModel::onDrawEvent,
                    modifier = Modifier.height(dimensionResource(id = R.dimen.Padding60)),
                    onShowStrokeWidthPopup = { showStrokeSettings = !showStrokeSettings },
                    setBackgroundImage = viewModel::setImage,
                    imageBitmap = viewModel.backgroundImage.value,
                    artMakerConfiguration = artMakerConfiguration,
                    onActivateEraser = { isEraserActive = !isEraserActive },
                    isEraserActive = isEraserActive,
                )
            }
        }
    }
}
