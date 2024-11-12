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
package io.fbada006.artmaker.composables

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
import io.fbada006.artmaker.ArtMakerUIState
import io.fbada006.artmaker.Res
import io.fbada006.artmaker.actions.ArtMakerAction
import io.fbada006.artmaker.actions.DrawEvent
import io.fbada006.artmaker.change_image
import io.fbada006.artmaker.clear_image
import io.fbada006.artmaker.customcolorpalette.CustomColorPalette
import io.fbada006.artmaker.dimensions.Dimensions
import io.fbada006.artmaker.icons.InkEraser
import io.fbada006.artmaker.icons.InkEraserOff
import io.fbada006.artmaker.icons.Redo
import io.fbada006.artmaker.icons.Undo
import io.fbada006.artmaker.models.ArtMakerConfiguration
import io.fbada006.artmaker.utils.ColorUtils
import io.fbada006.artmaker.utils.createPicker
import org.jetbrains.compose.resources.stringResource

/**
 * This is the bottom bar that allows for customisation/actions of color, stroke, eraser, undo/redo, clear, and background
 *
 * @param state
 * @param onAction called during a user action
 * @param onDrawEvent called when a drawing event happens
 * @param onShowStrokeWidthPopup called when stroke settings button is clicked
 * @param setBackgroundImage called to change the background image of the canvas
 * @param imageBitmap is the background image, if any
 * @param configuration global config
 * @param onActivateEraser called when eraser button is clicked
 * @param isEraserActive whether in erasing mode or not
 * @param modifier is the modifier applied to the entire menu
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ArtMakerControlMenu(
    state: ArtMakerUIState,
    onAction: (ArtMakerAction) -> Unit,
    onDrawEvent: (DrawEvent) -> Unit,
    onShowStrokeWidthPopup: () -> Unit,
    setBackgroundImage: (ImageBitmap?) -> Unit,
    imageBitmap: ImageBitmap?,
    configuration: ArtMakerConfiguration,
    onActivateEraser: () -> Unit,
    isEraserActive: Boolean,
    modifier: Modifier = Modifier,
) {
    val imagePicker = createPicker()
    /**
     * Before we Pick the Image, we first need to register the picker and set the background Image
     */
    imagePicker?.registerPicker { image ->
        setBackgroundImage(image)
    }
    var areImageOptionsExpanded by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }
    var showColorPalette by remember { mutableStateOf(false) }

    Surface(
        shadowElevation = Dimensions.ArtMakerControlMenuShadowElevation,
        modifier = modifier,
        color = configuration.controllerBackgroundColor,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(all = Dimensions.ArtMakerControlMenuPadding),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                MenuItem(
                    modifier = Modifier
                        .border(
                            width = Dimensions.ArtMakerColorPickerMenuItemBorderWidth,
                            brush = Brush.sweepGradient(colors = ColorUtils.COLOR_PICKER_DEFAULT_COLORS),
                            shape = RoundedCornerShape(size = Dimensions.ArtMakerColorPickerMenuItemShapeSize),
                        )
                        .padding(all = Dimensions.ArtMakerColorPickerMenuItemPadding),
                    imageVector = Icons.Filled.Circle,
                    onItemClicked = { showColorPicker = true },
                    colorTint = Color(state.strokeColour),
                    contentDescription = "Color Picker Icon"
                )
                MenuItem(
                    imageVector = Icons.Filled.Edit,
                    onItemClicked = {
                        onShowStrokeWidthPopup()
                    },
                    contentDescription = "Edit Icon"
                )
                MenuItem(
                    imageVector = if (isEraserActive) InkEraser else InkEraserOff,
                    onItemClicked = onActivateEraser,
                    enabled = state.canErase,
                    contentDescription = "Ink Eraser Icon"
                )
                MenuItem(
                    imageVector = Undo,
                    onItemClicked = {
                        onDrawEvent(DrawEvent.Undo)
                    },
                    enabled = state.canUndo,
                    contentDescription = "Undo Icon"
                )
                MenuItem(
                    imageVector = Redo,
                    onItemClicked = {
                        onDrawEvent(DrawEvent.Redo)
                    },
                    enabled = state.canRedo,
                    contentDescription = "Redo Icon"
                )
                MenuItem(
                    imageVector = Icons.Filled.Refresh,
                    onItemClicked = {
                        onDrawEvent(DrawEvent.Clear)
                    },
                    enabled = state.canClear,
                    contentDescription = "Refresh Icon"
                )
                MenuItem(
                    imageVector = Icons.Filled.Image,
                    onItemClicked = {
                        if (imageBitmap != null) {
                            areImageOptionsExpanded = true
                        } else {
                            imagePicker?.pickImage()
                        }
                    },
                    contentDescription = "Image Selector Icon"
                )
            }
            Box(
                Modifier
                    .padding(all = Dimensions.ArtMakerControlMenuDropDownPadding)
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
                            imagePicker?.pickImage()
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
                    artMakerConfiguration = configuration,
                )
            }

            if (showColorPalette) {
                ModalBottomSheet(
                    onDismissRequest = { showColorPalette = false },
                ) {
                    CustomColorPalette(
                        modifier = Modifier
                            .height(Dimensions.ArtMakerCustomColorPaletteHeight)
                            .padding(Dimensions.ArtMakerCustomColorPalettePadding)
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
    contentDescription: String?
) {
    val alpha = if (enabled) 1f else 0.5f
    IconButton(
        onClick = onItemClicked,
        modifier = Modifier.weight(1f, true),
        enabled = enabled,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = colorTint.copy(alpha = alpha),
            modifier = modifier.size(Dimensions.ArtMakerMenuItemSize),
        )
    }
}
