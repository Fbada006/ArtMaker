package com.artmaker.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.artmaker.state.ArtMakerUIState
import com.artmaker.viewmodels.ArtMakerViewModel

@Composable
internal fun ArtMakerStrokeWidthSlider(
    modifier: Modifier = Modifier,
    artMakerUIState: ArtMakerUIState,
    artMakerViewModel: ArtMakerViewModel,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxSize()
            .padding(all = 7.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = modifier.clickable {
                artMakerViewModel.decreaseStrokeWidth()
            },
            imageVector = Icons.Rounded.Remove, contentDescription = null, tint = Color.Black,
        )
        OutlinedTextField(
            modifier = modifier.width(width = 70.dp),
            value = artMakerUIState.strokeWidth.toString(),
            onValueChange = {
                // When changed...
            },
            shape = RoundedCornerShape(size = 21.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
        )
        Icon(
            modifier = modifier.clickable {
                artMakerViewModel.increaseStrokeWidth()
            },
            imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.Black,
        )
    }
}