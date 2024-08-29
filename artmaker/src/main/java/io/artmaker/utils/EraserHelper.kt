@file:JvmName("EraserHelper")

package io.artmaker.utils

import android.graphics.PointF
import androidx.compose.ui.geometry.Offset
import kotlin.math.pow

/**
 * Erases lines at the specified points.
 *
 * @param lines        Lines on which to run eraser.
 * @param eraseRadius  Eraser radius.
 * @param erasedPoints Array of eraser points. All lines intersecting circle with {@code eraserRadius} around these points will be erased.
 */
fun eraseLines(lines: List<List<Offset>>, eraseRadius: Float, vararg erasedPoints: PointF): Pair<List<List<Offset>>, List<Int>> {
    val hitRadiusSqr = eraseRadius.toDouble().pow(2.0).toFloat()
    val newLines = ArrayList<List<Offset>>(lines.size)
    val indexesToDelete = ArrayList<Int>()

    for (line in lines) {
        indexesToDelete.clear()

        // Collect points affected by the erase.
        for (i in line.indices) {
            val point = line[i]
            for (erasedPoint in erasedPoints) {
                if (getDistanceBetweenPointsSqr(point.x, point.y, erasedPoint.x, erasedPoint.y) < hitRadiusSqr) {
                    indexesToDelete.add(i)
                    break
                }
            }
        }

        if (indexesToDelete.isNotEmpty()) {
            var startIdx = 0
            // Split lines by deleted items.
            for (indexToDelete in indexesToDelete) {
                val length = indexToDelete - startIdx
                if (length > 0) {
                    newLines.add(line.subList(startIdx, indexToDelete))
                }
                startIdx = indexToDelete + 1
            }
            // Add remaining items.
            if (startIdx < line.size) {
                newLines.add(line.subList(startIdx, line.size))
            }
        } else {
            // Add unchanged line.
            newLines.add(line)
        }
    }

    return Pair(newLines, indexesToDelete)
}

fun getDistanceBetweenPointsSqr(aX: Float, aY: Float, bX: Float, bY: Float): Float {
    return (aX - bX) * (aX - bX) + (aY - bY) * (aY - bY)
}
