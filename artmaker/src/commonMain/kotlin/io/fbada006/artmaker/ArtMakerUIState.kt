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
package io.fbada006.artmaker

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/**
 * Determines the UI state
 */

data class ArtMakerUIState(
    val backgroundColour: Int = Color.White.toArgb(),
    val strokeWidth: Int = 5,
    val strokeColour: Int = Color.Red.toArgb(),
    val canRedo: Boolean = false,
    val canUndo: Boolean = false,
    val canClear: Boolean = false,
    val canErase: Boolean = false,
    val shouldUseStylusOnly: Boolean = false,
    val shouldDetectPressure: Boolean = false,
    val isStylusAvailable: Boolean = false,
    val canShowEnableStylusDialog: Boolean = true,
    val canShowDisableStylusDialog: Boolean = true,
)
