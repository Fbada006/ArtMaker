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