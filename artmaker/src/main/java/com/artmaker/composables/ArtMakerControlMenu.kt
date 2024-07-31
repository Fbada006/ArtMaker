package com.artmaker.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.artmaker.actions.ArtMakerAction
import com.artmaker.state.ArtMakerUIState

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
    modifier: Modifier = Modifier,
) {
    var showMoreOptions by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Surface(
        shadowElevation = 30.dp,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            MenuItem(
                imageVector = Icons.Filled.Circle,
                onItemClicked = { showColorPicker = true },
                colorTint = Color(state.strokeColour)
            )
            MenuItem(
                imageVector = Icons.Filled.Brush,
                onItemClicked = {
                    onAction(ArtMakerAction.SelectStrokeWidth)
                })
            MenuItem(
                imageVector = Icons.AutoMirrored.Filled.Undo,
                onItemClicked = {
                    onAction(ArtMakerAction.Undo)
                })
            MenuItem(
                imageVector = Icons.AutoMirrored.Filled.Redo,
                onItemClicked = {
                    onAction(ArtMakerAction.Redo)
                })
            MenuItem(
                imageVector = Icons.Filled.MoreVert,
                onItemClicked = { showMoreOptions = true })
        }
        if (showMoreOptions) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    showMoreOptions = false
                }
            ) {
                Row(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(10.dp),

                    ) {
                    MenuItem(
                        imageVector = Icons.Filled.FileUpload,
                        onItemClicked = {
                            onAction(ArtMakerAction.ExportArt)
                        })
                    MenuItem(
                        imageVector = Icons.Filled.Image,
                        onItemClicked = {
                            onAction(ArtMakerAction.UpdateBackground)
                        })
                }
            }
        }
        if (showColorPicker) {
            ColorPicker(
                onDismissRequest = { showColorPicker = false },
                defaultColor = state.strokeColour,
                onClick = { colorArgb ->
                    onAction(ArtMakerAction.SelectStrokeColour(Color(colorArgb)))
                }
            )
        }
    }
}

@Composable
private fun RowScope.MenuItem(
    imageVector: ImageVector,
    onItemClicked: () -> Unit,
    colorTint: Color = MaterialTheme.colorScheme.primary,
) {
    IconButton(
        onClick = onItemClicked, modifier = Modifier.weight(1f, true)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = colorTint,
            modifier = Modifier.size(32.dp)
        )
    }
}