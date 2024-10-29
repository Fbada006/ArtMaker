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
package io.fbada006.artmaker.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import io.fbada006.artmaker.DrawScreenState
import io.fbada006.artmaker.Res
import io.fbada006.artmaker.actions.ArtMakerAction
import io.fbada006.artmaker.actions.DrawEvent
import io.fbada006.artmaker.got_it
import io.fbada006.artmaker.image_bitmap
import io.fbada006.artmaker.models.ArtMakerConfiguration
import io.fbada006.artmaker.non_stylus_input_detected_message
import io.fbada006.artmaker.non_stylus_input_detected_title
import io.fbada006.artmaker.stylus_input_detected_message
import io.fbada006.artmaker.stylus_input_detected_title
import io.fbada006.artmaker.utils.isStylusInput
import io.fbada006.artmaker.utils.validateEvent
import org.jetbrains.compose.resources.stringResource

/**
 * [ArtMakerDrawScreen] is the composable that handles the drawing.
 */
@Composable
internal fun ArtMakerDrawScreen(
    modifier: Modifier = Modifier,
    artMakerConfiguration: ArtMakerConfiguration,
    onDrawEvent: (DrawEvent) -> Unit,
    onAction: (ArtMakerAction) -> Unit,
    state: DrawScreenState,
    isEraserActive: Boolean,
    eraserRadius: Float,
) {
    var bitmapHeight by rememberSaveable { mutableIntStateOf(0) }
    var bitmapWidth by rememberSaveable { mutableIntStateOf(0) }
    var shouldShowStylusDialog by rememberSaveable { mutableStateOf(false) }
    var stylusDialogType by rememberSaveable { mutableStateOf("") }
    var eraserPosition by remember { mutableStateOf<Offset?>(null) }
    var art by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(state.shouldTriggerArtExport) {
        if (state.shouldTriggerArtExport) {
            onAction(ArtMakerAction.ExportArt(art))
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(color = artMakerConfiguration.canvasBackgroundColor))
            .onSizeChanged { updatedSize ->
                val bitmapSize =
                    updatedSize.takeIf { it.height != 0 && it.width != 0 } ?: return@onSizeChanged
                bitmapHeight = bitmapSize.height
                bitmapWidth = bitmapSize.width
            }
            .pointerInput(state.shouldUseStylusOnly || isEraserActive) {
                awaitPointerEventScope {
                    var isDrawing = false

                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.first()

                        val offset = event.changes.first().position
                        val pressure = event.changes.first().pressure

                        when (event.type) {
                            PointerEventType.Press -> {
                                // Only update the stylus availability if this is a stylus input and the stylus availability state is not updated
                                if (change.isStylusInput() && !state.isStylusAvailable) {
                                    onAction(
                                        ArtMakerAction.UpdateStylusAvailability(
                                            change.isStylusInput(),
                                        ),
                                    )
                                }

                                getDialogType(change, state.shouldUseStylusOnly, state.isStylusAvailable)?.let { type ->
                                    shouldShowStylusDialog = true
                                    stylusDialogType = type
                                }

                                if (!change.validateEvent(state.shouldUseStylusOnly, state.isStylusAvailable)) {
                                    // Ignore this event
                                    continue
                                }

                                // Start a new shape when pressing
                                isDrawing = true

                                if (isEraserActive) {
                                    onDrawEvent(DrawEvent.Erase(offset))
                                } else {
                                    onDrawEvent(DrawEvent.AddNewShape(offset, pressure))
                                }
                            }

                            PointerEventType.Move -> {
                                if (isDrawing) {
                                    eraserPosition = offset

                                    if (isEraserActive) {
                                        onDrawEvent(DrawEvent.Erase(offset))
                                    } else {
                                        // Update the current shape
                                        onDrawEvent(DrawEvent.UpdateCurrentShape(offset, pressure))
                                    }
                                }
                            }

                            PointerEventType.Release -> {
                                isDrawing = false
                            }

                            PointerEventType.Unknown -> {
                                eraserPosition = null
                                onDrawEvent(DrawEvent.UndoLastShapePoint)
                            }
                        }
                    }
                }
            }
            .drawWithContent {
                drawContent()
                art = toImageBitmap(
                    bitmapWidth = bitmapWidth,
                    bitmapHeight = bitmapHeight,
                    state = state,
                    isEraserActive = isEraserActive,
                    eraserRadius = eraserRadius,
                    eraserPosition = eraserPosition,
                    pathList = state.pathList,
                )
            },
    ) {
        art?.let {
            Image(
                bitmap = it,
                contentDescription = stringResource(Res.string.image_bitmap),
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

    if (shouldShowStylusDialog && (state.canShowEnableStylusDialog || state.canShowDisableStylusDialog)) {
        if (stylusDialogType.isEmpty()) return
        val type = StylusDialogType.valueOf(stylusDialogType)
        val dialogInfo = when {
            state.canShowEnableStylusDialog && type == StylusDialogType.ENABLE_STYLUS_ONLY -> stringResource(
                Res.string.stylus_input_detected_title,
            ) to
                stringResource(Res.string.stylus_input_detected_message)

            state.canShowDisableStylusDialog && type == StylusDialogType.DISABLE_STYLUS_ONLY -> stringResource(
                Res.string.non_stylus_input_detected_title,
            ) to
                stringResource(Res.string.non_stylus_input_detected_message)

            else -> return
        }

        AlertDialog(
            icon = { Icon(imageVector = Icons.Filled.Edit, contentDescription = Icons.Filled.Edit.name) },
            title = { Text(text = dialogInfo.first) },
            text = { Text(text = dialogInfo.second) },
            onDismissRequest = { shouldShowStylusDialog = false },
            confirmButton = {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = {
                            shouldShowStylusDialog = false
                            when (type) {
                                StylusDialogType.ENABLE_STYLUS_ONLY -> onAction(ArtMakerAction.UpdateEnableStylusDialogShow(false))
                                StylusDialogType.DISABLE_STYLUS_ONLY -> onAction(ArtMakerAction.UpdateDisableStylusDialogShow(false))
                            }
                        },
                    ) {
                        Text(text = stringResource(Res.string.got_it))
                    }
                }
            },
        )
    }
}

private fun getDialogType(change: PointerInputChange, useStylusOnly: Boolean, isStylusAvailable: Boolean) = when {
    change.isStylusInput() && !useStylusOnly -> StylusDialogType.ENABLE_STYLUS_ONLY.name
    !change.validateEvent(useStylusOnly, isStylusAvailable) -> StylusDialogType.DISABLE_STYLUS_ONLY.name
    else -> null
}

internal enum class StylusDialogType { ENABLE_STYLUS_ONLY, DISABLE_STYLUS_ONLY }
