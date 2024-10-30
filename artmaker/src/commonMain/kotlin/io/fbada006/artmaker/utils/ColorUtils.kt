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
package io.fbada006.artmaker.utils

import androidx.compose.ui.graphics.Color

/**
 * This is a list of colours that are used in the project (Color Picker) as a part of its functionality.
 */

object ColorUtils {
    val COLOR_PICKER_DEFAULT_COLORS = listOf(
        Color.Red,
        Color.Blue,
        Color.Black,
        Color.Yellow,
        Color.Cyan,
        Color.Gray,
        Color.Green,
        Color.Magenta,
        Color.DarkGray,
        Color(color = 0xFFA020F0), // A purple
    )
}
