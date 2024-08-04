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

import android.graphics.Matrix
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.core.math.MathUtils.clamp
import com.artmaker.actions.DrawEvent
import com.artmaker.models.PointsData
import com.artmaker.state.ArtMakerUIState
import com.artmaker.viewmodels.ArtMakerViewModel

/**
 * [ArtMakerDrawScreen] Composable where we will implement the draw logic.
 * For now, this is just an empty blue screen.
 */
@Composable
internal fun ArtMakerDrawScreen(
    modifier: Modifier = Modifier,
    state: ArtMakerUIState,
    onDrawEvent: (DrawEvent) -> Unit,
    viewModel: ArtMakerViewModel,
    imageBitmap: ImageBitmap? = null,

    ) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    // Used to clip the y value from the Offset during drawing so that the canvas cannot draw into the control menu
    // Add an extra 2dp for line visibility
    val yOffset = with(density) { (CONTROL_MENU_HEIGHT + 2.dp).toPx() }
    val screenHeightPx = with(density) { screenHeight.toPx() }
    val clippedScreenHeight = screenHeightPx - yOffset
    var canvas: Canvas? = null

    DisposableEffect(key1 = viewModel) {
        viewModel.setImage(imageBitmap)

        onDispose {
            viewModel.releaseBitmap()
            viewModel.clearImageBitmap()
        }
    }
    Canvas(
        modifier = modifier
            .background(color = Color(color = state.backgroundColour))
            .onSizeChanged { updatedSize ->
                val size =
                    updatedSize.takeIf { it.height != 0 && it.width != 0 } ?: return@onSizeChanged
                viewModel.releaseBitmap()
                viewModel.pathBitmap =
                    ImageBitmap(size.width, size.height, ImageBitmapConfig.Argb8888)
                        .also { imageBitmap ->
                            canvas = Canvas(imageBitmap)
                            viewModel.bitmapSize.value = size
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
            drawIntoCanvas { canvas->
                viewModel.imageBit?.let {
                    val shader = it.value?.let { it1 -> ImageShader(it1, TileMode.Clamp) }
                    val brush = shader?.let { it1 -> ShaderBrush(it1) }
                    val shaderMatrix = Matrix()
                    // cache the paint in the internal stack.
                    if (brush != null) {
                        drawRect(brush = brush, size = viewModel.bitmapSize.value.toSize())
                    }
//                    canvas.restore()
                    viewModel.imageBitmapMatrix.value = shaderMatrix
                    viewModel.pathBitmap?.let { bitmap ->
                        canvas.drawImage(bitmap, Offset.Zero, Paint())
                    }
                }
                viewModel.pathList.forEach { data->
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
