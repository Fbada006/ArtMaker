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

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import io.fbada006.artmaker.actions.ArtMakerAction
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun ShareableContent(shouldExport: Boolean, onAction: (ArtMakerAction) -> Unit, content: @Composable () -> Unit, modifier: Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }
    val graphicsLayer = rememberGraphicsLayer()

    val writeStorageAccessState = rememberMultiplePermissionsState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // No permissions are needed on Android 10+ to add files in the shared storage
            emptyList()
        } else {
            listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        },
    )

    LaunchedEffect(shouldExport) {
        if (shouldExport) {
            if (writeStorageAccessState.allPermissionsGranted) {
                val bitmap = graphicsLayer.toImageBitmap()
                onAction(ArtMakerAction.ExportArt(bitmap))
            } else if (writeStorageAccessState.shouldShowRationale) {
                launch {
                    val result = snackbarHostState.showSnackbar(
                        message = "The storage permission is needed to save the image.",
                        actionLabel = "Grant access",
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        writeStorageAccessState.launchMultiplePermissionRequest()
                    }
                }
            } else {
                writeStorageAccessState.launchMultiplePermissionRequest()
            }
        }
    }
    /**
     * Remove this implementation and the corresponding iOS implementation
     * once Compose KMP has support for the graphics layer. This will allow for
     * more shared UI code between Android and iOS. Expected to be available
     * in Compose KMP 1.7.1 (next release from the time of writing this comment).
     */
    Box(
        modifier
            .drawWithCache {
                onDrawWithContent {
                    graphicsLayer.record {
                        this@onDrawWithContent.drawContent()
                    }
                    drawLayer(graphicsLayer)
                }
            },
    ) {
        content()
    }
}
