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

import android.content.Context
import android.content.Intent
import android.content.Intent.createChooser
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.ContextCompat.startActivity
import io.fbada006.artmaker.Res
import io.fbada006.artmaker.share_your_image
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume

private suspend fun scanFilePath(context: Context, filePath: String): Uri? = suspendCancellableCoroutine { continuation ->
    MediaScannerConnection.scanFile(
        context,
        arrayOf(filePath),
        arrayOf("image/png"),
    ) { _, scannedUri ->
        if (scannedUri == null) {
            continuation.cancel(
                Exception(
                    "File $filePath could not be scanned",
                ),
            )
        } else {
            continuation.resume(scannedUri)
        }
    }
}

suspend fun getUriFromBitmap(context: Context, bitmap: ImageBitmap?): Uri {
    val androidBitmap = bitmap?.asAndroidBitmap()
    val files = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        "screenshot-${System.currentTimeMillis()}.png",
    )

    files.writeBitmap(androidBitmap, Bitmap.CompressFormat.PNG, 100)

    return scanFilePath(context, files.path) ?: throw Exception("File could not be saved")
}

private fun File.writeBitmap(bitmap: Bitmap?, format: Bitmap.CompressFormat, quality: Int) {
    outputStream().use { out ->
        bitmap?.compress(format, quality, out)
        out.flush()
    }
}

actual suspend fun shareImage(imageBitmap: ImageBitmap?) {
    val context = ContextProvider.getContext()
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, getUriFromBitmap(context, imageBitmap))
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    val chooser = createChooser(intent, Res.string.share_your_image.toString()).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(context, chooser, null)
}
