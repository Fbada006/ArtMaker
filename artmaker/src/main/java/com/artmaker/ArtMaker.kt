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
package com.artmaker

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.BuildCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.artmaker.composables.ArtMakerControlMenu
import com.artmaker.composables.ArtMakerDrawScreen
import com.artmaker.composables.CONTROL_MENU_HEIGHT
import com.artmaker.viewmodels.ArtMakerViewModel

/**
 * [ArtMaker] composable which has our draw screen and controllers
 * We will expose this composable and test our Library on the app layer
 */
@OptIn(BuildCompat.PrereleaseSdkCheck::class)
@Composable
fun ArtMaker(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val artMakerViewModel: ArtMakerViewModel = viewModel(
        factory = ArtMakerViewModel.provideFactory(context = context),
    )
    val artMakerUIState by artMakerViewModel.artMakerUIState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        val size = artMakerViewModel.bitmapSize.value
        val combinedBitmap = ImageBitmap(size.width, size.height, ImageBitmapConfig.Argb8888)
        val canvas = Canvas(combinedBitmap)
        artMakerViewModel.imageBit.value?.let {
            val immutableBitmap = it.asAndroidBitmap().copy(Bitmap.Config.ARGB_8888, false)
            canvas.nativeCanvas.drawBitmap(immutableBitmap, artMakerViewModel.imageBitmapMatrix.value, null)
            immutableBitmap.recycle()
        }
        artMakerViewModel.pathBitmap?.let { canvas.drawImage(it, Offset.Zero, Paint()) }
    }
    Column(modifier = modifier) {
        ArtMakerDrawScreen(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            state = artMakerUIState,
            onDrawEvent = artMakerViewModel::onDrawEvent,
            viewModel = artMakerViewModel
        )
        ArtMakerControlMenu(
            onAction = artMakerViewModel::onAction,
            state = artMakerUIState,
            modifier = Modifier.height(CONTROL_MENU_HEIGHT),
            viewModel = artMakerViewModel,
        )
    }
}
