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
package com.artmaker.state

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/**
 * This is a Data Class that represents the UI State.
 * Dummy values have been used for testing purposes but they are of course subject to change.
 */
data class ArtMakerUIState(
    val backgroundColour: Int = Color.Blue.toArgb(),
    val strokeWidth: Int = 3,
    val strokeColour: Int = Color.Red.toArgb(),
)
