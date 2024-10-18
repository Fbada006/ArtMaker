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
package com.fbada006.shared

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fbada006.shared.actions.ArtMakerAction
import com.fbada006.shared.actions.ExportType
import com.fbada006.shared.composables.ArtMakerControlMenu
import com.fbada006.shared.composables.ArtMakerDrawScreen
import com.fbada006.shared.composables.StrokeSettings
import com.fbada006.shared.data.CustomColorsManager
import com.fbada006.shared.data.PreferencesManager
import com.fbada006.shared.dimensions.Dimensions
import com.fbada006.shared.drawing.DrawingManager
import com.fbada006.shared.models.ArtMakerConfiguration

/**
 * [ArtMaker] has the draw screen as well as control menu (the bar offering customisation options). By default, it is a white screen that allows the user
 * to draw any shape and customise the attributes of the drawing.
 *
 * @param modifier is the [Modifier]
 * @param onFinishDrawing is the callback exposed once the user clicks done (the [FloatingActionButton] with the checkmark) to trigger finish drawing
 * @param artMakerConfiguration is the configuration to customise the appearance of the control menu and other values
 */
@Composable
fun ArtMaker(
    modifier: Modifier = Modifier,
    onFinishDrawing: (ImageBitmap) -> Unit = {},
    artMakerConfiguration: ArtMakerConfiguration = ArtMakerConfiguration(),
) {
    val viewModel: ArtMakerViewModel = viewModel {
        ArtMakerViewModel(
            customColorsManager = CustomColorsManager(),
            preferencesManager = PreferencesManager(),
            drawingManager = DrawingManager(),
        )
    }

    var showStrokeSettings by remember { mutableStateOf(value = false) }
    val state by viewModel.uiState.collectAsState()
    val shouldTriggerArtExport by viewModel.shouldTriggerArtExport.collectAsState()
    val finishedImage by viewModel.finishedImage.collectAsState()
    var isFullScreenEnabled by remember { mutableStateOf(false) }
    var isEraserActive by remember { mutableStateOf(value = false) }
    LaunchedEffect(key1 = finishedImage) {
        finishedImage?.let { onFinishDrawing(it) }
    }

    LaunchedEffect(key1 = state.canErase) {
        if (!state.canErase) {
            isEraserActive = false
        }
    }

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                visible = viewModel.pathList.isNotEmpty() && !showStrokeSettings,
                modifier = Modifier.padding(
                    bottom = if (isFullScreenEnabled) Dimensions.FullScreenEnabledPadding else Dimensions.FullScreenDisabledPadding,
                ),
            ) {
                Column {
                    // Trigger sharing the image. It has to save the image first
                    if (artMakerConfiguration.canShareArt) {
                        FloatingActionButton(onClick = { viewModel.onAction(ArtMakerAction.TriggerArtExport(ExportType.ShareImage)) }) {
                            Icon(imageVector = Icons.Filled.Share, contentDescription = Icons.Filled.Share.name)
                        }
                    }
                    Spacer(modifier = Modifier.height(Dimensions.ExportArtButtonSpacerHeight))
                    // Finish the drawing and hand it back to the calling application as a bitmap
                    FloatingActionButton(onClick = { viewModel.onAction(ArtMakerAction.TriggerArtExport(ExportType.FinishDrawingImage)) }) {
                        Icon(imageVector = Icons.Filled.Done, contentDescription = Icons.Filled.Done.name)
                    }
                    Spacer(modifier = Modifier.height(Dimensions.FullScreenToggleButtonSpacerHeight))
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
                state = DrawScreenState(
                    pathList = viewModel.pathList,
                    shouldTriggerArtExport = shouldTriggerArtExport,
                    backgroundImage = viewModel.backgroundImage.value,
                    isFullScreenMode = isFullScreenEnabled,
                    shouldUseStylusOnly = state.shouldUseStylusOnly,
                    shouldDetectPressure = state.shouldDetectPressure,
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
                        .padding(
                            top = Dimensions.StrokeSettingsTopPadding,
                            end = Dimensions.StrokeSettingsEndPadding,
                            start = Dimensions.StrokeSettingsStartPadding,
                        ),
                    shouldUseStylusOnly = state.shouldUseStylusOnly,
                    shouldDetectPressure = state.shouldDetectPressure,
                )
            }
            AnimatedVisibility(visible = !isFullScreenEnabled) {
                ArtMakerControlMenu(
                    state = state,
                    onAction = viewModel::onAction,
                    onDrawEvent = viewModel::onDrawEvent,
                    modifier = Modifier.height(Dimensions.ArtMakerControlMenuHeight),
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
