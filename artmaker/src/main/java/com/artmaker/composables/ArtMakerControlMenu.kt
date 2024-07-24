package com.artmaker.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.artmaker.artmaker.R

/**
 * We can add pass the controller inside this composable and remove the function types.
 * We can also pass the logic somewhere else and leave the ArtMakerControlMenu
 * without any functionality.
 */
@Composable
internal fun ArtMakerControlMenu(
    onExportArt: () -> Unit,
    onUnDoActionClicked: () -> Unit,
    onRedoActionClicked: () -> Unit,
    onClearActionClicked: () -> Unit,
    onUpdateBackgroundActionClicked: () -> Unit,
    onColorSelected: () -> Unit
) {
    Row(
        modifier = Modifier.padding(12.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        MenuItems(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_download),
            onItemClicked = onExportArt,
            colorTint = MaterialTheme.colorScheme.primary
        )
        MenuItems(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_brush),
            onItemClicked = onColorSelected,
            colorTint = MaterialTheme.colorScheme.primary
        )
        MenuItems(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_undo),
            onItemClicked = onUnDoActionClicked,
            colorTint = MaterialTheme.colorScheme.primary
        )
        MenuItems(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_redo),
            onItemClicked = onRedoActionClicked,
            colorTint = MaterialTheme.colorScheme.primary
        )
        MenuItems(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_restore),
            onItemClicked = onClearActionClicked,
            colorTint = MaterialTheme.colorScheme.primary
        )
        MenuItems(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_add_image),
            onItemClicked = onUpdateBackgroundActionClicked,
            colorTint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun RowScope.MenuItems(
    imageVector: ImageVector,
    onItemClicked: () -> Unit,
    colorTint: Color,
    border: Boolean = false,
) {
    val modifier = Modifier.size(24.dp)
    IconButton(
        onClick = onItemClicked, modifier = Modifier.weight(1f, true)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = colorTint,
            modifier = if (border) modifier.border(
                0.5.dp,
                Color.White,
                shape = CircleShape
            ) else modifier
        )
    }
}