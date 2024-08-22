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
@file: JvmName("MotionEventUtil")

package io.artmaker.utils

import android.content.Context
import android.hardware.input.InputManager
import android.view.MotionEvent
import java.util.Locale

/**
 * Check if the input is from a stylus or not during drawing
 */
internal fun MotionEvent.isStylusInput(): Boolean = this.getToolType(this.actionIndex) == MotionEvent.TOOL_TYPE_STYLUS

/**
 * Validate the logic for the auto detection of the stylus to determine whether to only use stylus input for palm rejection
 */
internal fun MotionEvent.validateEvent(context: Context, useStylusOnly: Boolean): Boolean {
    if (!isStylusConnected(context)) return true // No stylus
    val isStylusDrawing = this.isStylusInput()

    // Check if stylus drawing is required but not detected
    return !useStylusOnly || isStylusDrawing
}

private fun isStylusConnected(context: Context): Boolean {
    val inputManager = context.getSystemService(Context.INPUT_SERVICE) as InputManager
    val inputDeviceIds: IntArray = inputManager.inputDeviceIds
    return inputDeviceIds.any {
        val device = inputManager.getInputDevice(it)
        device?.name?.lowercase(Locale.ROOT)?.contains("pen") == true
    }
}
