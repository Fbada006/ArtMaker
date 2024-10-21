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
package com.fbada006.shared.utils

import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerType

/**
 * Check if the input is from a stylus or not during drawing
 */
internal fun PointerInputChange.isStylusInput(): Boolean = this.type == PointerType.Stylus

/**
 * Validate the logic for the auto detection of the stylus to determine whether to only use stylus input for palm rejection
 */
internal fun PointerInputChange.validateEvent(useStylusOnly: Boolean, isStylusAvailable: Boolean): Boolean {
    if (!isStylusAvailable) return true // No stylus

    // Check if stylus drawing is required but not detected
    return !useStylusOnly || this.isStylusInput()
}
