package com.fbada006.shared.utils

lateinit var myFunc: () -> Boolean

actual fun isStylusConnected(): Boolean {
    return myFunc()
}
