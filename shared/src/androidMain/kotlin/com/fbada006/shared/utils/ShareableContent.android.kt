package com.fbada006.shared.utils

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
import com.fbada006.shared.actions.ArtMakerAction
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
                        message = "A message",
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