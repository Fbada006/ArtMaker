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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.math.MathUtils.clamp
import com.artmaker.models.PointsData
import com.artmaker.state.ArtMakerUIState


// A place holder for now that will be replaced with the actual controller
internal class TestController {
    private val _pathList = mutableStateListOf<PointsData>()
    val pathList: SnapshotStateList<PointsData> = _pathList

    fun addNewShape(offset: Offset) {
        val data = PointsData(mutableStateListOf(offset))
        _pathList.add(data)
    }

    fun updateCurrentShape(offset: Offset) {
        val idx = _pathList.lastIndex
        _pathList[idx].points.add(offset)
    }

    fun undoLastShapePoint() {
        val idx = _pathList.lastIndex
        _pathList[idx].points.removeLast()
    }
}

/**
 * [ArtMakerDrawScreen] Composable where we will implement the draw logic.
 * For now, this is just an empty blue screen.
 */
@Composable
internal fun ArtMakerDrawScreen(
    modifier: Modifier = Modifier,
    artMakerUIState: ArtMakerUIState
) {

    val density = LocalDensity.current
    val controller = TestController()
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    // Used to clip the y value from the Offset during drawing so that the canvas cannot draw into the control menu
    // The value 62 dp is obtained from height of control menu 60dp plus an extra 2dp for line visibility
    val yOffset = with(density) { 62.dp.toPx() }
    val screenHeightPx = with(density) { screenHeight.toPx() }
    val clippedScreenHeight = screenHeightPx - yOffset

    Canvas(
        modifier = modifier
            .background(color = Color(color = artMakerUIState.backgroundColour))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        controller.addNewShape(offset)
                    },
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        controller.addNewShape(offset)
                    },
                    onDragCancel = {
                        controller.undoLastShapePoint()
                    },
                ) { change, _ ->
                    val offset = change.position
                    val clampedOffset =
                        Offset(x = offset.x, y = clamp(offset.y, 0f, clippedScreenHeight))
                    controller.updateCurrentShape(clampedOffset)
                }
            },
        onDraw = {
            controller.pathList.forEach { data ->
                drawPoints(
                    points = data.points,
                    pointMode = if (data.points.size == 1) PointMode.Points else PointMode.Polygon, // Draw a point if the shape has only one item otherwise a free flowing shape
                    color = data.strokeColor,
                    strokeWidth = data.strokeWidth,
                    alpha = data.alpha,
                )
            }
        },
    )
}