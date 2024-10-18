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
package com.fbada006.shared.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import com.fbada006.shared.DrawScreenState
import com.fbada006.shared.actions.ArtMakerAction
import com.fbada006.shared.actions.DrawEvent
import com.fbada006.shared.models.ArtMakerConfiguration
import com.fbada006.shared.models.alpha
import com.fbada006.shared.utils.isStylusInput
import com.fbada006.shared.utils.validateEvent
import io.fbada006.artmaker.Res
import io.fbada006.artmaker.got_it
import io.fbada006.artmaker.non_stylus_input_detected_message
import io.fbada006.artmaker.non_stylus_input_detected_title
import io.fbada006.artmaker.stylus_input_detected_message
import io.fbada006.artmaker.stylus_input_detected_title
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
//    val context = LocalContext.current
//    val density = LocalDensity.current
//    val configuration = LocalConfiguration.current

//    val screenHeight = configuration.screenHeightDp.dp
    // Used to clip the y value from the Offset during drawing so that the canvas cannot draw into the control menu
    // Add an extra 2dp for line visibility. In full screen mode, we do not need to offset anything
//    val yOffset = if (state.isFullScreenMode) {
//        0f
//    } else {
//        with(density) {
//            62.dp.toPx()
// //            (dimensionResource(id = R.dimen.Padding60) + dimensionResource(id = R.dimen.Padding2)).toPx()
//        }
//    }
//    val screenHeightPx = with(density) { screenHeight.toPx() }
//    val maxDrawingHeight = screenHeightPx - yOffset
    var bitmapHeight by rememberSaveable { mutableIntStateOf(0) }
    var bitmapWidth by rememberSaveable { mutableIntStateOf(0) }
    var shouldShowStylusDialog by rememberSaveable { mutableStateOf(false) }
    var stylusDialogType by rememberSaveable { mutableStateOf("") }
    var eraserPosition by remember { mutableStateOf<Offset?>(null) }

//    val graphicsLayer = rememberGraphicsLayer()
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    val writeStorageAccessState = rememberMultiplePermissionsState(
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            // No permissions are needed on Android 10+ to add files in the shared storage
//            emptyList()
//        } else {
//            listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        },
//    )

//    LaunchedEffect(key1 = state.shouldTriggerArtExport) {
//        if (state.shouldTriggerArtExport) {
//            if (writeStorageAccessState.allPermissionsGranted) {
//                val bitmap = graphicsLayer.toImageBitmap()
//                onAction(com.fbada006.shared.actions.ArtMakerAction.ExportArt(bitmap))
//            } else if (writeStorageAccessState.shouldShowRationale) {
//                launch {
//                    val result = snackbarHostState.showSnackbar(
//                        message = context.getString(R.string.the_storage_permission_is_needed_to_save_the_image),
//                        actionLabel = context.getString(R.string.grant_access),
//                    )
//
//                    if (result == SnackbarResult.ActionPerformed) {
//                        writeStorageAccessState.launchMultiplePermissionRequest()
//                    }
//                }
//            } else {
//                writeStorageAccessState.launchMultiplePermissionRequest()
//            }
//        }
//    }

    Canvas(
        modifier = modifier
            .background(color = Color(color = artMakerConfiguration.canvasBackgroundColor))
            .onSizeChanged { updatedSize ->
                val bitmapSize =
                    updatedSize.takeIf { it.height != 0 && it.width != 0 } ?: return@onSizeChanged
                bitmapHeight = bitmapSize.height
                bitmapWidth = bitmapSize.width
            }
//            .drawWithCache {
//                onDrawWithContent {
//                    graphicsLayer.record {
//                        this@onDrawWithContent.drawContent()
//                    }
//                    drawLayer(graphicsLayer)
//                }
//            }
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
                                getDialogType(change, state.shouldUseStylusOnly)?.let { type ->
                                    shouldShowStylusDialog = true
                                    stylusDialogType = type
                                }

                                if (!change.validateEvent(state.shouldUseStylusOnly)) {
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
            },
        onDraw = {
            drawIntoCanvas {
                state.backgroundImage?.let { bitmap ->
                    val shader = ImageShader(bitmap, TileMode.Clamp)
                    val brush = ShaderBrush(shader)
                    drawRect(
                        brush = brush,
                        size = Size(bitmapWidth.toFloat(), bitmapHeight.toFloat()),
                    )
                }

                state.pathList.forEach { data ->
                    drawPoints(
                        points = data.points,
                        // Draw a point if the shape has only one item otherwise a free flowing shape
                        pointMode = if (data.points.size == 1) PointMode.Points else PointMode.Polygon,
                        color = data.strokeColor,
                        alpha = data.alpha(state.shouldDetectPressure),
                        strokeWidth = data.strokeWidth,
                    )
                }
                if (isEraserActive) {
                    eraserPosition?.let { position ->
                        drawCircle(
                            color = Color.Gray,
                            radius = eraserRadius,
                            center = position,
                            style = Stroke(
                                width = 8.0f,
                            ),
                        )
                    }
                }
            }
        },
    )

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

private fun getDialogType(change: PointerInputChange, useStylusOnly: Boolean) = when {
    change.isStylusInput() && !useStylusOnly -> StylusDialogType.ENABLE_STYLUS_ONLY.name
    !change.validateEvent(useStylusOnly) -> StylusDialogType.DISABLE_STYLUS_ONLY.name
    else -> null
}

internal enum class StylusDialogType { ENABLE_STYLUS_ONLY, DISABLE_STYLUS_ONLY }
