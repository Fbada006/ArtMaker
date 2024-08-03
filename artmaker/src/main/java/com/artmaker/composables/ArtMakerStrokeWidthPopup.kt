package com.artmaker.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.artmaker.state.ArtMakerUIState

@Composable
internal fun ArtMakerStrokeWidthPopup(
    onDismissRequest: (Int) -> Unit,
    artMakerUIState: ArtMakerUIState,
) {
    Dialog(
        onDismissRequest = { onDismissRequest(artMakerUIState.strokeWidth) },
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
        ),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .padding(14.dp),
            shape = RoundedCornerShape(21.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
        ) {
            ArtMakerStrokeWidthSlider(
                artMakerUIState = artMakerUIState,
            )
        }
    }
}