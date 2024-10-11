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
package com.fbada006.shared.composables

//import android.annotation.SuppressLint
//import android.graphics.BitmapFactory
//import android.graphics.ImageDecoder
//import android.os.Build
//import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhonelinkErase
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
//import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.dimensionResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.res.vectorResource
//import androidx.core.os.BuildCompat
//import com.artmaker.customcolorpalette.CustomColorPalette
//import com.google.modernstorage.photopicker.PhotoPicker
import com.fbada006.shared.ArtMakerUIState
import com.fbada006.shared.actions.ArtMakerAction
import com.fbada006.shared.actions.DrawEvent
import com.fbada006.shared.models.ArtMakerConfiguration
import com.fbada006.shared.utils.ColorUtils
//import io.fbada006.artmaker.R

private const val IMAGE_PICKER_MAX_ITEMS = 1

/**
 * We can add the controller as a constructor to [ArtMakerControlMenu]  composable and remove the function types.
 * As an alternative we could add the logic to the ArtMaker and leave the [ArtMakerControlMenu]
 * without any functionality.
 */
//@OptIn(BuildCompat.PrereleaseSdkCheck::class, ExperimentalMaterial3Api::class)
//@SuppressLint("UnsafeOptInUsageError")
@Composable
internal fun ArtMakerControlMenu(
    state: ArtMakerUIState,
    onAction: (ArtMakerAction) -> Unit,
    onDrawEvent: (DrawEvent) -> Unit,
    modifier: Modifier = Modifier,
    onShowStrokeWidthPopup: () -> Unit,
    setBackgroundImage: (ImageBitmap?) -> Unit,
    imageBitmap: ImageBitmap?,
    artMakerConfiguration: ArtMakerConfiguration,
    onActivateEraser: () -> Unit,
    isEraserActive: Boolean,
) {
    var areImageOptionsExpanded by remember { mutableStateOf(false) }
//    val context = LocalContext.current
//    val photoPicker =
//        rememberLauncherForActivityResult(PhotoPicker()) { uris ->
//            val uri = uris.firstOrNull() ?: return@rememberLauncherForActivityResult
//            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                // For Android 9.0 (API level 28) and above
//                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
//            } else {
//                // For Android versions below 9.0
//                BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
//            }
//            setBackgroundImage(bitmap.asImageBitmap())
//        }
//    var showColorPicker by remember { mutableStateOf(false) }
    var showColorPalette by remember { mutableStateOf(false) }

    Surface(
        shadowElevation =60.dp,
        modifier = modifier,
        color = artMakerConfiguration.controllerBackgroundColor,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(all = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                MenuItem(
                    modifier = Modifier
                        .border(
                            width =2.dp,
                            brush = Brush.sweepGradient(colors = ColorUtils.COLOR_PICKER_DEFAULT_COLORS),
                            shape = RoundedCornerShape(size = 32.dp),
                        )
                        .padding(all = 2.dp),
                    imageVector = Icons.Filled.Circle,
                    onItemClicked = { },
                    colorTint = Color(state.strokeColour),
                )
                MenuItem(
                    imageVector = Icons.Filled.Edit,
                    onItemClicked = {
                        onShowStrokeWidthPopup()
                    },
                )
                MenuItem(
                    imageVector = if (isEraserActive) {
                        Icons.Default.Check

                    } else {
                        Icons.Default.PhonelinkErase
                    },
                    onItemClicked = onActivateEraser,
                    enabled = state.canErase,
                )
                MenuItem(
                    imageVector = Icons.AutoMirrored.Filled.Undo,
                    onItemClicked = {
                        onDrawEvent(DrawEvent.Undo)
                    },
                    enabled = state.canUndo,
                )
                MenuItem(
                    imageVector = Icons.AutoMirrored.Filled.Redo,
                    onItemClicked = {
                        onDrawEvent(DrawEvent.Redo)
                    },
                    enabled = state.canRedo,
                )
                MenuItem(
                    imageVector = Icons.Filled.Refresh,
                    onItemClicked = {
                        onDrawEvent(DrawEvent.Clear)
                    },
                    enabled = state.canClear,
                )
                MenuItem(
                    imageVector = Icons.Filled.Image,
                    onItemClicked = {
                        if (imageBitmap != null) {
                            areImageOptionsExpanded = true
                        } else {
//                            photoPicker.launch(
//                                PhotoPicker.Args(
//                                    PhotoPicker.Type.IMAGES_ONLY,
//                                    IMAGE_PICKER_MAX_ITEMS,
//                                ),
//                            )
                        }
                    },
                )
            }
            Box(
                Modifier
                    .padding(all = 12.dp)
                    .align(Alignment.End),
            ) {
                DropdownMenu(
                    expanded = areImageOptionsExpanded,
                    onDismissRequest = {
                        areImageOptionsExpanded = false
                    },
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(text ="change image")
                        },
                        onClick = {
                            // Launch the picker with only one image selectable
//                            photoPicker.launch(
//                                PhotoPicker.Args(
//                                    PhotoPicker.Type.IMAGES_ONLY,
//                                    IMAGE_PICKER_MAX_ITEMS,
//                                ),
//                            )
                            areImageOptionsExpanded = false
                        },
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text ="Clear Image")
                        },
                        onClick = {
                            setBackgroundImage(null)
                            areImageOptionsExpanded = false
                        },
                    )
                }
            }
//            if (showColorPicker) {
//                ColorPicker(
//                    onDismissRequest = { showColorPicker = false },
//                    defaultColor = state.strokeColour,
//                    onClick = { colorArgb ->
//                        onAction(ArtMakerAction.SelectStrokeColour(Color(colorArgb)))
//                        showColorPicker = false
//                    },
//                    onColorPaletteClick = {
//                        showColorPicker = false
//                        showColorPalette = true
//                    },
//                    artMakerConfiguration = artMakerConfiguration,
//                )
//            }

//            if (showColorPalette) {
//                ModalBottomSheet(
//                    onDismissRequest = { showColorPalette = false },
//                ) {
//                    CustomColorPalette(
//                        modifier = Modifier
//                            .height(dimensionResource(R.dimen.color_palette_height))
//                            .padding(dimensionResource(R.dimen.Padding12)),
//                        onAccept = {
//                            onAction(ArtMakerAction.SelectStrokeColour(Color(it.toArgb()), isCustomColor = true))
//                            showColorPalette = false
//                        },
//                        onCancel = {
//                            showColorPalette = false
//                            showColorPicker = true
//                        },
//                    )
//                }
//            }
        }
    }
}

@Composable
private fun RowScope.MenuItem(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onItemClicked: () -> Unit,
    colorTint: Color = MaterialTheme.colorScheme.primary,
    enabled: Boolean = true,
) {
    IconButton(
        onClick = onItemClicked,
        modifier = Modifier.weight(1f, true),
        enabled = enabled,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = colorTint.copy(alpha = (if (enabled) 1f else 0.5f)),
            modifier = modifier.size(size = 32.dp),
        )
    }
}
