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
    val alpha: Float = 1f,
)
