package com.artmaker.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.math.MathUtils.clamp
import com.artmaker.models.PointsData

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
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val controller = TestController()
    val displayMetrics = context.resources.displayMetrics

    // Used to clip the y value from the Offset during drawing so that the canvas cannot draw into the control menu
    val yOffset = with(density) { 100.dp.toPx() }
    val clippedScreenHeight = displayMetrics.heightPixels - yOffset

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        controller.addNewShape(offset)
                    }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        controller.addNewShape(offset)
                    },
                    onDragCancel = {
                        controller.undoLastShapePoint()
                    }
                ) { change, _ ->
                    val offset = change.position
                    val clampedOffset = Offset(x = offset.x, y = clamp(offset.y, 0f, clippedScreenHeight))
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
        }
    )
}
