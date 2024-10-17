package com.fbada006.shared.utils

lateinit var doesIosDeviceHaveAStylus: () -> Boolean

actual fun isStylusConnected(): Boolean {
    return doesIosDeviceHaveAStylus()
}
