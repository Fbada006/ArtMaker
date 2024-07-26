package com.artmaker.models

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

/**
 * This class will hold each shape drawn on screen be it a single dot or multiple shapes drawn
 * on screen. The values defined here for the characteristic of the shape drawn on screen are configurable
 */
internal data class PointsData(
    var points: SnapshotStateList<Offset>,
    val strokeWidth: Float = 15f,
    val strokeColor: Color = Color.Red,
    val alpha: Float = 1f
)