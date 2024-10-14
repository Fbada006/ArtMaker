package com.fbada006.shared.utils

import android.content.Context
import android.hardware.input.InputManager
import java.util.Locale

actual fun isStylusConnected(): Boolean {
    val context = ContextProvider.getContext()

    val inputManager = context.getSystemService(Context.INPUT_SERVICE) as InputManager
    val inputDeviceIds: IntArray = inputManager.inputDeviceIds
    return inputDeviceIds.any {
        val device = inputManager.getInputDevice(it)
        device?.name?.lowercase(Locale.ROOT)?.contains("pen") == true
    }
}
