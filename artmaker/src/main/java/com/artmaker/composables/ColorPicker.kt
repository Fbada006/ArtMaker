package com.artmaker.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.artmaker.utils.ColorUtils

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ColorPicker(onDismissRequest: () -> Unit, modifier: Modifier = Modifier) {

    val sheetState = rememberModalBottomSheetState()
    var customColor by rememberSaveable { mutableIntStateOf(ColorUtils.COLOR_PICKER_DEFAULT_COLORS[2].toArgb()) }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                FlowRow(
                    modifier = Modifier
                        .padding(vertical = 4.dp),
//                    horizontalArrangement = Arrangement.spacedBy(pickerValues.colorGridSpacing),
//                    verticalArrangement = Arrangement.spacedBy(pickerValues.colorGridSpacing),
//                    maxItemsInEachRow = NUM_COLUMNS
                ) {
                    repeat(ColorUtils.COLOR_PICKER_DEFAULT_COLORS.size) { colorIndex ->
                        val color = ColorUtils.COLOR_PICKER_DEFAULT_COLORS[colorIndex]
                        val colorArgb = color.toArgb()
                        Box(
                            modifier = Modifier
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(color)
                                    .clickable {
                                        customColor = colorArgb
                                    }
                            )

                            if (colorArgb == customColor) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}