@file:JvmName("EraserHelper")

package io.artmaker.utils

import android.graphics.PointF
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import io.artmaker.models.PointsData
import kotlin.math.pow

/**
 * Erases pointData at the specified points.
 * @param pointsData Lines on which to run eraser.
 * @param eraseRadius  Eraser radius.
 * @param erasedPoints Array of eraser points. All lines intersecting circle with {@code eraserRadius} around these points will be erased.
 */
fun eraseLines(pointsData: List<PointsData>, eraseRadius: Float = 40F, vararg erasedPoints: PointF): List<PointsData> {
    val hitRadiusSqr = eraseRadius.toDouble().pow(2.0).toFloat()
    val newPointData = ArrayList<PointsData>(pointsData.size)
    val indexesToDelete = ArrayList<Int>()

    for (pd in pointsData) {
        indexesToDelete.clear()
        // Collect points affected by the erase.
        for (i in pd.points.indices) {
            val point = pd.points[i]
            for (erasedPoint in erasedPoints) {
                if (getDistanceBetweenPointsSqr(point.x, point.y, erasedPoint.x, erasedPoint.y) < hitRadiusSqr) {
                    indexesToDelete.add(i)
                    break
                }
            }
        }

        if (indexesToDelete.isNotEmpty()) {
            var startIdx = 0
            // Split Points by deleted items.
            for (indexToDelete in indexesToDelete) {
                val length = indexToDelete - startIdx
                if (length > 0) {
                    val newPoints = SnapshotStateList<Offset>()
                    newPoints.addAll(pd.points.subList(startIdx, indexToDelete))
                    newPointData.add(
                        PointsData(
                            points = newPoints,
                            strokeWidth = pd.strokeWidth,
                            strokeColor = pd.strokeColor,
                            alphas = pd.alphas,
                        ),
                    )
                }
                startIdx = indexToDelete + 1
            }
            // Add remaining items.
            if (startIdx < pd.points.size) {
                val newPoints = SnapshotStateList<Offset>()
                newPoints.addAll(pd.points.subList(startIdx, pd.points.size))
                newPointData.add(
                    PointsData(
                        points = newPoints,
                        strokeWidth = pd.strokeWidth,
                        strokeColor = pd.strokeColor,
                        alphas = pd.alphas,
                    ),
                )
            }
        } else {
            // Add unchanged line.
            newPointData.add(pd)
        }
    }

    return newPointData
}

fun getDistanceBetweenPointsSqr(aX: Float, aY: Float, bX: Float, bY: Float): Float {
    return (aX - bX) * (aX - bX) + (aY - bY) * (aY - bY)
}
