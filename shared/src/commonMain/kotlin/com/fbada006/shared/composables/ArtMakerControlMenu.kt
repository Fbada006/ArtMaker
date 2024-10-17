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
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.fbada006.shared.ArtMakerUIState
import com.fbada006.shared.actions.ArtMakerAction
import com.fbada006.shared.actions.DrawEvent
import com.fbada006.shared.icons.InkEraser
import com.fbada006.shared.icons.InkEraserOff
import com.fbada006.shared.icons.Redo
import com.fbada006.shared.icons.Undo
import com.fbada006.shared.models.ArtMakerConfiguration
import com.fbada006.shared.utils.ColorUtils
import com.fbada006.shared.utils.ImagePicker
import customcolorpalette.CustomColorPalette
import io.fbada006.artmaker.Res
import io.fbada006.artmaker.change_image
import io.fbada006.artmaker.clear_image
import org.jetbrains.compose.resources.stringResource

/**
 * We can add the controller as a constructor to [ArtMakerControlMenu]  composable and remove the function types.
 * As an alternative we could add the logic to the ArtMaker and leave the [ArtMakerControlMenu]
 * without any functionality.
 */
@OptIn(ExperimentalMaterial3Api::class)
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
    imagePicker: ImagePicker
) {
    /**
     * Before we Pick the Image, we first need to register the picker and set the background Image
     */
    imagePicker.registerPicker { image->
        setBackgroundImage(image)
    }
    var areImageOptionsExpanded by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }
    var showColorPalette by remember { mutableStateOf(false) }

    Surface(
        shadowElevation = 60.dp,
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
                            width = 2.dp,
                            brush = Brush.sweepGradient(colors = ColorUtils.COLOR_PICKER_DEFAULT_COLORS),
                            shape = RoundedCornerShape(size = 32.dp),
                        )
                        .padding(all = 2.dp),
                    imageVector = Icons.Filled.Circle,
                    onItemClicked = { showColorPicker = true },
                    colorTint = Color(state.strokeColour),
                )
                MenuItem(
                    imageVector = Icons.Filled.Edit,
                    onItemClicked = {
                        onShowStrokeWidthPopup()
                    },
                )
                MenuItem(
                    imageVector = if (isEraserActive) InkEraser else InkEraserOff,
                    onItemClicked = onActivateEraser,
                    enabled = state.canErase,
                )
                MenuItem(
                    imageVector = Undo,
                    onItemClicked = {
                        onDrawEvent(DrawEvent.Undo)
                    },
                    enabled = state.canUndo,
                )
                MenuItem(
                    imageVector = Redo,
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
                            imagePicker.pickImage()
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
                            Text(text = stringResource(Res.string.change_image))
                        },
                        onClick = {
                            imagePicker.pickImage()
                            areImageOptionsExpanded = false
                        },
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(Res.string.clear_image))
                        },
                        onClick = {
                            setBackgroundImage(null)
                            areImageOptionsExpanded = false
                        },
                    )
                }
            }
            if (showColorPicker) {
                ColorPicker(
                    onDismissRequest = { showColorPicker = false },
                    defaultColor = state.strokeColour,
                    onClick = { colorArgb ->
                        onAction(ArtMakerAction.SelectStrokeColour(Color(colorArgb)))
                        showColorPicker = false
                    },
                    onColorPaletteClick = {
                        showColorPicker = false
                        showColorPalette = true
                    },
                    artMakerConfiguration = artMakerConfiguration,
                )
            }

            if (showColorPalette) {
                ModalBottomSheet(
                    onDismissRequest = { showColorPalette = false },
                ) {
                    CustomColorPalette(
                        modifier = Modifier
                            .height(330.dp)
                            .padding(12.dp)
                            .navigationBarsPadding(),
                        onAccept = {
                            onAction(ArtMakerAction.SelectStrokeColour(Color(it.toArgb()), isCustomColor = true))
                            showColorPalette = false
                        },
                        onCancel = {
                            showColorPalette = false
                            showColorPicker = true
                        },
                    )
                }
            }
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
    val alpha = if (enabled) 1f else 0.5f
    IconButton(
        onClick = onItemClicked,
        modifier = Modifier.weight(1f, true),
        enabled = enabled,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = colorTint.copy(alpha = alpha),
            modifier = modifier.size(32.dp),
        )
    }
}
