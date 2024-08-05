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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.math.MathUtils.clamp
import com.artmaker.actions.DrawEvent
import com.artmaker.models.PointsData
import com.artmaker.state.ArtMakerUIState
import com.artmaker.viewmodels.ArtMakerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Stack

/**
 * [ArtMakerDrawScreen] Composable where we will implement the draw logic.
 * For now, this is just an empty blue screen.
 */
@Composable
internal fun ArtMakerDrawScreen(
    modifier: Modifier = Modifier,
    state: ArtMakerUIState,
    onDrawEvent: (DrawEvent) -> Unit,
    drawPath: Stack<PointsData>,
    artMakerViewModel: ArtMakerViewModel,
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    // Initializes a mutable state to track invalidations, triggering redraws.
    val invalidatorTick: MutableState<Int> = remember { mutableIntStateOf(0) }

    val screenHeight = configuration.screenHeightDp.dp
    // Used to clip the y value from the Offset during drawing so that the canvas cannot draw into the control menu
    // Add an extra 2dp for line visibility
    val yOffset = with(density) { (CONTROL_MENU_HEIGHT + 2.dp).toPx() }
    val screenHeightPx = with(density) { screenHeight.toPx() }
    val clippedScreenHeight = screenHeightPx - yOffset

    val coroutineScope = rememberCoroutineScope()
    /**
     * SideEffect block to collect changes from artMakerViewModel.reviseTick and trigger redraws.
     */
    SideEffect {
        coroutineScope.launch(Dispatchers.Main) {
            artMakerViewModel.reviseTick.collect {
                invalidatorTick.value++
            }
        }
    }
    Canvas(
        modifier = modifier
            .background(color = Color(color = state.backgroundColour))
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
                    // Triggers a redraw after each drag gesture.
                    invalidatorTick.value++
                }
            },
        onDraw = {
            if (invalidatorTick.value != 0) {
                drawPath.forEach { data ->
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
