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
package com.artmaker.composables

import android.Manifest
import android.os.Build
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.core.math.MathUtils.clamp
import com.artmaker.actions.ArtMakerAction
import com.artmaker.actions.DrawEvent
import com.artmaker.artmaker.R
import com.artmaker.models.PointsData
import com.artmaker.state.ArtMakerUIState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch

/**
 * [ArtMakerDrawScreen] Composable where we will implement the draw logic.
 * For now, this is just an empty blue screen.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun ArtMakerDrawScreen(
    modifier: Modifier = Modifier,
    state: ArtMakerUIState,
    onDrawEvent: (DrawEvent) -> Unit,
    onAction: (ArtMakerAction) -> Unit,
    pathList: SnapshotStateList<PointsData>,
    imageBitmap: ImageBitmap?,
    shouldTriggerArtExport: Boolean,
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    // Used to clip the y value from the Offset during drawing so that the canvas cannot draw into the control menu
    // Add an extra 2dp for line visibility
    val yOffset = with(density) {
        (dimensionResource(id = R.dimen.control_menu_height) + dimensionResource(
            id = R.dimen.extra_visibility,
        )).toPx()
    }
    val screenHeightPx = with(density) { screenHeight.toPx() }
    val clippedScreenHeight = screenHeightPx - yOffset
    var bitmapHeight by rememberSaveable {
        mutableIntStateOf(0)
    }
    var bitmapWidth by rememberSaveable {
        mutableIntStateOf(0)
    }

    val graphicsLayer = rememberGraphicsLayer()
    val snackbarHostState = remember { SnackbarHostState() }

    val writeStorageAccessState = rememberMultiplePermissionsState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // No permissions are needed on Android 10+ to add files in the shared storage
            emptyList()
        } else {
            listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        },
    )

    val context = LocalContext.current

    LaunchedEffect(key1 = shouldTriggerArtExport) {
        if (shouldTriggerArtExport) {
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
            .background(color = Color(color = state.backgroundColour))
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
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        onDrawEvent(DrawEvent.AddNewShape(offset))
                    },
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        onDrawEvent(DrawEvent.AddNewShape(offset))
                    },
                    onDragCancel = {
                        onDrawEvent(DrawEvent.UndoLastShapePoint)
                    },
                ) { change, _ ->
                    val offset = change.position
                    val clampedOffset =
                        Offset(x = offset.x, y = clamp(offset.y, 0f, clippedScreenHeight))
                    onDrawEvent(DrawEvent.UpdateCurrentShape(clampedOffset))
                }
            },
        onDraw = {
            drawIntoCanvas { canvas ->
                imageBitmap?.let { imageBitmap ->
                    val shader = ImageShader(imageBitmap, TileMode.Clamp)
                    val brush = ShaderBrush(shader)
                    drawRect(
                        brush = brush,
                        size = Size(bitmapWidth.toFloat(), bitmapHeight.toFloat()),
                    )
                }
                pathList.forEach { data ->
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
}
