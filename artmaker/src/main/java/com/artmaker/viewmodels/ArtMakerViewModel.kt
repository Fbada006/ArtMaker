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
package com.artmaker.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artmaker.actions.ArtMakerAction
import com.artmaker.actions.DrawEvent
import com.artmaker.models.PointsData
import com.artmaker.sharedpreferences.ArtMakerSharedPreferences
import com.artmaker.sharedpreferences.PreferenceKeys
import com.artmaker.state.ArtMakerUIState
import com.google.modernstorage.photopicker.PhotoPicker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class ArtMakerViewModel(
    private val preferences: ArtMakerSharedPreferences,
) : ViewModel() {

    private var _artMakerUIState =
        MutableStateFlow(value = ArtMakerUIState(strokeColour = preferences.get(PreferenceKeys.SELECTED_STROKE_COLOUR, 0)))
    val artMakerUIState = _artMakerUIState.asStateFlow()

    private val _pathList = mutableStateListOf<PointsData>()
    val pathList: SnapshotStateList<PointsData> = _pathList


    /** An [ImageBitmap] to draw a bitmap on the canvas as a background. */
//    internal var imageBitmap: ImageBitmap? = null
    private val _imageBit:MutableState<ImageBitmap?> = mutableStateOf(null)
    val imageBit = _imageBit

    /** An [ImageBitmap] to draw paths on the canvas. */
    internal var pathBitmap: ImageBitmap? = null

    internal val bitmapSize: MutableState<IntSize> = mutableStateOf(IntSize(0, 0))

    internal val imageBitmapMatrix: MutableState<Matrix> = mutableStateOf(Matrix())


    fun onAction(action: ArtMakerAction) {
        when (action) {
            ArtMakerAction.ExportArt -> exportArt()
            ArtMakerAction.Redo -> redo()
            ArtMakerAction.Undo -> undo()
            ArtMakerAction.Clear -> clear()
            ArtMakerAction.UpdateBackground -> updateBackgroundColour()
            is ArtMakerAction.SelectStrokeColour -> updateStrokeColor(colour = action.color)
            ArtMakerAction.SelectStrokeWidth -> selectStrokeWidth()
        }
    }

    fun onDrawEvent(event: DrawEvent) {
        when (event) {
            is DrawEvent.AddNewShape -> addNewShape(event.offset)
            DrawEvent.UndoLastShapePoint -> undoLastShapePoint()
            is DrawEvent.UpdateCurrentShape -> updateCurrentShape(event.offset)
        }
    }

    private fun addNewShape(offset: Offset) {
        val data = PointsData(points = mutableStateListOf(offset), strokeColor = Color(artMakerUIState.value.strokeColour))
        _pathList.add(data)
    }

    private fun updateCurrentShape(offset: Offset) {
        val idx = _pathList.lastIndex
        _pathList[idx].points.add(offset)
    }

    private fun undoLastShapePoint() {
        val idx = _pathList.lastIndex
        _pathList[idx].points.removeLast()
    }

    private fun exportArt() {}

    private fun redo() {}

    private fun undo() {}

    private fun clear() {}

    private fun updateBackgroundColour() {}

    private fun updateStrokeColor(colour: Color) {
        preferences.set(
            key = PreferenceKeys.SELECTED_STROKE_COLOUR,
            value = colour.toArgb(),
        )
        _artMakerUIState.update {
            it.copy(strokeColour = preferences.get(PreferenceKeys.SELECTED_STROKE_COLOUR, 0))
        }
    }
    /** Sets an [ImageBitmap] to draw on the canvas as a background. */
    fun setImage(bitmap: ImageBitmap?) {
        _imageBit.value = bitmap
    }

    /** Clear the image bitmap. */
    fun clearImageBitmap() {
        setImage(null)
        imageBitmapMatrix.value = Matrix()
    }

    internal fun releaseBitmap() {
        pathBitmap?.asAndroidBitmap()?.recycle()
        pathBitmap = null
    }

//    fun getImageBitmap(): ImageBitmap {
//        val size = bitmapSize.value
//        val combinedBitmap = ImageBitmap(size.width, size.height, ImageBitmapConfig.Argb8888)
//        val canvas = Canvas(combinedBitmap)
//        _imageBit.value?.let {
//            val immutableBitmap = it.asAndroidBitmap().copy(Bitmap.Config.ARGB_8888, false)
//            canvas.nativeCanvas.drawBitmap(immutableBitmap, imageBitmapMatrix.value, null)
//            immutableBitmap.recycle()
//        }
//        pathBitmap?.let { canvas.drawImage(it, Offset.Zero, Paint()) }
//        return combinedBitmap
//    }
    private fun selectStrokeWidth() {}

    companion object {
        fun provideFactory(
            context: Context,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ArtMakerViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ArtMakerViewModel(
                        preferences = ArtMakerSharedPreferences(
                            context = context,
                        ),
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel Class")
            }
        }
    }
}
