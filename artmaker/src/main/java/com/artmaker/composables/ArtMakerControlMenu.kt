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
import androidx.compose.material.icons.filled.Refresh
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

/**
 * We can add the controller as a constructor to [ArtMakerControlMenu]  composable and remove the function types.
 * As an alternative we could add the logic to the ArtMaker and leave the [ArtMakerControlMenu]
 * without any functionality.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ArtMakerControlMenu(
  onStrokeWidthActionClicked: () -> Unit,
  onUnDoActionClicked: () -> Unit,
  onRedoActionClicked: () -> Unit,
  onClearActionClicked: () -> Unit,
  onUpdateBackgroundActionClicked: () -> Unit,
  onExportFileActionClicked: () -> Unit,
  onColorSelected: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var showBottomSheet by remember { mutableStateOf(false) }
  val sheetState = rememberModalBottomSheetState()
  Surface(
    shadowElevation = 30.dp,
    modifier = modifier,
  ) {
    Row(
      modifier = Modifier
        .navigationBarsPadding()
        .padding(10.dp),
      horizontalArrangement = Arrangement.SpaceAround,
    ) {
      MenuItem(
        imageVector = Icons.Filled.Circle,
        onItemClicked = onStrokeWidthActionClicked,
      )
      MenuItem(
        imageVector = Icons.Filled.Brush,
        onItemClicked = onColorSelected,
      )
      MenuItem(
        imageVector = Icons.AutoMirrored.Filled.Undo,
        onItemClicked = onUnDoActionClicked,
      )
      MenuItem(
        imageVector = Icons.AutoMirrored.Filled.Redo,
        onItemClicked = onRedoActionClicked,
      )
      MenuItem(
        imageVector = Icons.Filled.Refresh,
        onItemClicked = onClearActionClicked,
      )
      MenuItem(
        imageVector = Icons.Filled.MoreVert,
        onItemClicked = {
          showBottomSheet = true
        },
      )
    }
    if (showBottomSheet) {
      ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
          showBottomSheet = false
        },
      ) {
        Row(
          modifier = Modifier
            .navigationBarsPadding()
            .padding(10.dp),

        ) {
          MenuItem(
            imageVector = Icons.Filled.FileUpload,
            onItemClicked = onExportFileActionClicked,
          )
          MenuItem(
            imageVector = Icons.Filled.Image,
            onItemClicked = onUpdateBackgroundActionClicked,
          )
        }
      }
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
    onClick = onItemClicked,
    modifier = Modifier.weight(1f, true),
  ) {
    Icon(
      imageVector = imageVector,
      contentDescription = null,
      tint = colorTint,
      modifier = Modifier.size(32.dp),
    )
  }
}
