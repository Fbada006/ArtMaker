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
package io.artmaker

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.ImageBitmap
import io.artmaker.models.PointsData

/**
 * Provides state from the [io.artmaker.composables.ArtMakerDrawScreen] screen
 */
internal data class DrawState(
    val pathList: SnapshotStateList<PointsData>,
    val backgroundImage: ImageBitmap?,
    val shouldTriggerArtExport: Boolean,
    val isFullScreenMode: Boolean,
    val shouldUseStylusOnly: Boolean,
    val canShowEnableStylusDialog: Boolean,
    val canShowDisableStylusDialog: Boolean,
)
