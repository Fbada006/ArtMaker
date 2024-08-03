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

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.artmaker.actions.ArtMakerAction
import com.artmaker.state.ArtMakerUIState

/**
 * This is a popup in the form of a [Dialog] that displays the ArtMakerStrokeWidthSlider, title, and accompanying buttons.
 */

@Composable
internal fun ArtMakerStrokeWidthPopup(
    onDismissRequest: () -> Unit,
    artMakerUIState: ArtMakerUIState,
    onAction: (ArtMakerAction) -> Unit,
) {
    var sliderPosition by remember { mutableIntStateOf(value = artMakerUIState.strokeWidth) }
    val context = LocalContext.current
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
        ),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 350.dp)
                .padding(all = 7.dp),
            shape = RoundedCornerShape(size = 21.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    text = "Drag to select Stroke Width",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                )
                ArtMakerStrokeWidthSlider(
                    sliderPosition = sliderPosition.toFloat(),
                    onValueChange = {
                        sliderPosition = it.toInt()
                    },
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Button(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    ) {
                        Text(
                            text = "Cancel",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Button(
                        onClick = {
                            onAction(ArtMakerAction.SelectStrokeWidth(strokeWidth = sliderPosition))
                            onDismissRequest()
                            Toast.makeText(
                                context,
                                "Stroke Width has been successfully selected!",
                                Toast.LENGTH_SHORT,
                            ).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    ) {
                        Text(
                            text = "Ok",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}