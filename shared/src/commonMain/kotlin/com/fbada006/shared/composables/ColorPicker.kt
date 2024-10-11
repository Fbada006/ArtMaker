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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.fbada006.shared.data.CustomColorsManager
import com.fbada006.shared.models.ArtMakerConfiguration
import com.fbada006.shared.utils.ColorUtils

private const val NUM_COLUMNS = 5
typealias ColorArgb = Int

private const val LUMINANCE_THRESHOLD = 0.5

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun ColorPicker(
    onDismissRequest: () -> Unit,
    defaultColor: Int,
    onClick: (ColorArgb) -> Unit,
    onColorPaletteClick: () -> Unit,
    artMakerConfiguration: ArtMakerConfiguration,
) {
//    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val customColorsManager = remember { CustomColorsManager() }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        containerColor = Color.LightGray,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
                ) {
                    Column {
                        val customColors by customColorsManager.getColors().collectAsState(listOf())
                        val allColors = artMakerConfiguration.pickerCustomColors.ifEmpty {
                            ColorUtils.COLOR_PICKER_DEFAULT_COLORS
                        }

                        FlowRow(
                            modifier = Modifier
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(space = 4.dp),
                            maxItemsInEachRow = NUM_COLUMNS,
                        ) {
                            repeat(allColors.size) { colorIndex ->
                                val color = allColors[colorIndex].toArgb()
                                ColorItem(color, defaultColor, onClick)
                            }
                        }

                        // Only display the custom colors if we have any
                        if (customColors.isNotEmpty()) {
                            Text(
                                text = "Recent colours",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(vertical = 4.dp),
                            )

                            FlowRow(
                                modifier = Modifier
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(space = 4.dp),
                                maxItemsInEachRow = NUM_COLUMNS,
                            ) {
                                repeat(customColors.size) { colorIndex ->
                                    val color = customColors[colorIndex]
                                    ColorItem(color, defaultColor, onClick)
                                }
                            }
                        }
                    }

                    // Custom color picker
                    Box(
                        modifier = Modifier
                            .size(size = 48.dp)
                            .clip(RoundedCornerShape(size = 8.dp))
                            .background(brush = Brush.sweepGradient(colors = ColorUtils.COLOR_PICKER_DEFAULT_COLORS))
                            .clickable { onColorPaletteClick() }
                            .align(Alignment.CenterVertically),
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorItem(color: Int, defaultColor: Int, onClick: (ColorArgb) -> Unit) {
    var selectedColor by rememberSaveable { mutableIntStateOf(defaultColor) }
    Box(
        modifier = Modifier,
    ) {
        Box(
            modifier = Modifier
                .size(size = 48.dp)
                .clip(RoundedCornerShape(size = 8.dp))
                .background(Color(color))
                .clickable {
                    selectedColor = color
                    onClick(color)
                },
        )

        if (color == selectedColor) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.Black
//                if (androidx.core.graphics.ColorUtils.calculateLuminance(
//                        color,
//                    ) > LUMINANCE_THRESHOLD
//                ) {
//                    Color.Black
//                } else {
//                    Color.White
//                }
                ,
                modifier = Modifier
                    .align(Alignment.Center),
            )
        }
    }
}
