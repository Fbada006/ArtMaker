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
@file:JvmName("EraserHelper")

package io.artmaker.utils

import android.graphics.PointF
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import io.artmaker.models.PointsData
import kotlin.math.pow

private const val MIN_ERASE_RADIUS = 20F
private const val MAX_ERASE_RADIUS = 40F

/**
 * Erases pointData at the specified points.
 * @param pointsData Lines on which to run eraser.
 * @param eraseRadius  Eraser radius.
 * @param erasedPoints Array of eraser points. All lines intersecting circle with {@code eraserRadius} around these points will be erased.
 */
internal fun erasePointData(pointsData: List<PointsData>, eraseRadius: Float, vararg erasedPoints: PointF): List<PointsData> {
    val hitEraseRadius = eraseRadius.coerceIn(MIN_ERASE_RADIUS, MAX_ERASE_RADIUS)
    val hitRadiusSqr = hitEraseRadius.toDouble().pow(2.0).toFloat()
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

private fun getDistanceBetweenPointsSqr(aX: Float, aY: Float, bX: Float, bY: Float): Float {
    return (aX - bX) * (aX - bX) + (aY - bY) * (aY - bY)
}
