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
package io.artmaker.composables

import android.Manifest
import android.content.Context
import android.os.Build
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.math.MathUtils.clamp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import io.artmaker.DrawState
import io.artmaker.actions.ArtMakerAction
import io.artmaker.actions.DrawEvent
import io.artmaker.models.ArtMakerConfiguration
import io.artmaker.utils.isStylusInput
import io.artmaker.utils.validateEvent
import io.fbada006.artmaker.R
import kotlinx.coroutines.launch

/**
 * [ArtMakerDrawScreen] is the composable that handles the drawing.
 */
@OptIn(ExperimentalPermissionsApi::class, ExperimentalComposeUiApi::class)
@Composable
internal fun ArtMakerDrawScreen(
    modifier: Modifier = Modifier,
    artMakerConfiguration: ArtMakerConfiguration,
    onDrawEvent: (DrawEvent) -> Unit,
    onAction: (ArtMakerAction) -> Unit,
    state: DrawState,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    // Used to clip the y value from the Offset during drawing so that the canvas cannot draw into the control menu
    // Add an extra 2dp for line visibility. In full screen mode, we do not need to offset anything
    val yOffset = if (state.isFullScreenMode) {
        0f
    } else {
        with(density) {
            (dimensionResource(id = R.dimen.Padding60) + dimensionResource(id = R.dimen.Padding2)).toPx()
        }
    }
    val screenHeightPx = with(density) { screenHeight.toPx() }
    val maxDrawingHeight = screenHeightPx - yOffset
    var bitmapHeight by rememberSaveable { mutableIntStateOf(0) }
    var bitmapWidth by rememberSaveable { mutableIntStateOf(0) }
    var shouldShowStylusDialog by rememberSaveable { mutableStateOf(false) }
    var stylusDialogType by rememberSaveable { mutableStateOf("") }

    val graphicsLayer = rememberGraphicsLayer()
    val snackbarHostState = remember { SnackbarHostState() }

    var isDrawing by remember { mutableStateOf(false) }

    val writeStorageAccessState = rememberMultiplePermissionsState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // No permissions are needed on Android 10+ to add files in the shared storage
            emptyList()
        } else {
            listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        },
    )

    LaunchedEffect(key1 = state.shouldTriggerArtExport) {
        if (state.shouldTriggerArtExport) {
            if (writeStorageAccessState.allPermissionsGranted) {
                val bitmap = graphicsLayer.toImageBitmap()
                onAction(ArtMakerAction.ExportArt(bitmap))
            } else if (writeStorageAccessState.shouldShowRationale) {
                launch {
                    val result = snackbarHostState.showSnackbar(
                        message = context.getString(R.string.the_storage_permission_is_needed_to_save_the_image),
                        actionLabel = context.getString(R.string.grant_access),
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        writeStorageAccessState.launchMultiplePermissionRequest()
                    }
                }
            } else {
                writeStorageAccessState.launchMultiplePermissionRequest()
            }
        }
    }

    Canvas(
        modifier = modifier
            .background(color = Color(color = artMakerConfiguration.canvasBackgroundColor))
            .onSizeChanged { updatedSize ->
                val bitmapSize =
                    updatedSize.takeIf { it.height != 0 && it.width != 0 } ?: return@onSizeChanged
                bitmapHeight = bitmapSize.height
                bitmapWidth = bitmapSize.width
            }
            .drawWithCache {
                onDrawWithContent {
                    graphicsLayer.record {
                        this@onDrawWithContent.drawContent()
                    }
                    drawLayer(graphicsLayer)
                }
            }
            .pointerInteropFilter { event ->
                val offset = Offset(event.x, event.y)
                val pressure = event.getPressure(event.actionIndex)
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        getDialogType(context, event, state.shouldUseStylusOnly)?.let { type ->
                            shouldShowStylusDialog = true
                            stylusDialogType = type
                        }

                        if (!event.validateEvent(context, state.shouldUseStylusOnly)) return@pointerInteropFilter false
                        isDrawing = true
                        onDrawEvent(DrawEvent.AddNewShape(offset, pressure))
                    }

                    MotionEvent.ACTION_MOVE -> {
                        isDrawing = true
                        val clampedOffset =
                            Offset(x = offset.x, y = clamp(offset.y, 0f, maxDrawingHeight))
                        onDrawEvent(DrawEvent.UpdateCurrentShape(clampedOffset))
                    }

                    MotionEvent.ACTION_CANCEL -> {
                        isDrawing = false
                        onDrawEvent(DrawEvent.UndoLastShapePoint)
                    }
                }
                true
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
                        pointMode = if (data.points.size == 1) PointMode.Points else PointMode.Polygon, // Draw a point if the shape has only one item otherwise a free flowing shape
                        color = data.strokeColor,
                        strokeWidth = data.strokeWidth,
                        alpha = data.alpha,
                    )
                }
            }
        },
    )

    if (shouldShowStylusDialog && (state.canShowEnableStylusDialog || state.canShowDisableStylusDialog)) {
        if (stylusDialogType.isEmpty()) return
        val type = StylusDialogType.valueOf(stylusDialogType)
        val dialogInfo = when {
            state.canShowEnableStylusDialog && type == StylusDialogType.ENABLE_STYLUS_ONLY -> stringResource(R.string.stylus_input_detected_title) to stringResource(R.string.stylus_input_detected_message)
            state.canShowDisableStylusDialog && type == StylusDialogType.DISABLE_STYLUS_ONLY -> stringResource(R.string.non_stylus_input_detected_title) to stringResource(R.string.non_stylus_input_detected_message)
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
                        Text(text = stringResource(id = R.string.got_it))
                    }
                }
            },
        )
    }
}

private fun getDialogType(context: Context, event: MotionEvent, useStylusOnly: Boolean) = when {
    event.isStylusInput() && !useStylusOnly -> StylusDialogType.ENABLE_STYLUS_ONLY.name
    !event.validateEvent(context, useStylusOnly) -> StylusDialogType.DISABLE_STYLUS_ONLY.name
    else -> null
}

internal enum class StylusDialogType { ENABLE_STYLUS_ONLY, DISABLE_STYLUS_ONLY }
