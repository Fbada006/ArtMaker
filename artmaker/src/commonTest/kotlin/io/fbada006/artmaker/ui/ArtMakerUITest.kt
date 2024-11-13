package io.fbada006.artmaker.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.fbada006.artmaker.ArtMakerUIState
import io.fbada006.artmaker.DrawScreenState
import io.fbada006.artmaker.composables.ArtMakerControlMenu
import io.fbada006.artmaker.composables.ArtMakerDrawScreen
import io.fbada006.artmaker.composables.ColorPicker
import io.fbada006.artmaker.composables.LineStyle
import io.fbada006.artmaker.composables.Slider
import io.fbada006.artmaker.composables.StrokePreview
import io.fbada006.artmaker.composables.StrokeState
import io.fbada006.artmaker.customcolorpalette.CustomColorPalette
import io.fbada006.artmaker.models.ArtMakerConfiguration
import org.jetbrains.skia.Color
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ArtMakerUITest {

    @Test
    fun testArtMakerControlMenu() = runComposeUiTest {

        setContent {
            ArtMakerControlMenu(
                state = ArtMakerUIState(),
                onAction = {},
                onDrawEvent = {},
                onShowStrokeWidthPopup = {},
                setBackgroundImage = {},
                imageBitmap = null,
                configuration = ArtMakerConfiguration(),
                onActivateEraser = {},
                isEraserActive = true,
            )
        }

        onNodeWithContentDescription(label = "Color Picker Icon").assertExists()
        onNodeWithContentDescription(label = "Color Picker Icon").performClick()
        onNodeWithContentDescription(label = "Edit Icon").assertExists()
        onNodeWithContentDescription(label = "Edit Icon").performClick()
        onNodeWithContentDescription(label = "Ink Eraser Icon").assertExists()
        onNodeWithContentDescription(label = "Ink Eraser Icon").performClick()
        onNodeWithContentDescription(label = "Undo Icon").assertExists()
        onNodeWithContentDescription(label = "Undo Icon").performClick()
        onNodeWithContentDescription(label = "Redo Icon").assertExists()
        onNodeWithContentDescription(label = "Redo Icon").performClick()
        onNodeWithContentDescription(label = "Refresh Icon").assertExists()
        onNodeWithContentDescription(label = "Refresh Icon").performClick()
        onNodeWithContentDescription(label = "Image Selector Icon").assertExists()
        onNodeWithContentDescription(label = "Image Selector Icon").performClick()

    }

    @Test
    fun testArtMakerDrawScreen() = runComposeUiTest {

        setContent {
            ArtMakerDrawScreen(
                configuration = ArtMakerConfiguration(),
                onDrawEvent = {},
                onAction = {},
                state = DrawScreenState(
                    pathList = mutableStateListOf(),
                    backgroundImage = null,
                    backgroundColor = Color.RED,
                    shouldTriggerArtExport = true,
                    isFullScreenMode = true,
                    isStylusAvailable = true,
                    shouldUseStylusOnly = true,
                    shouldDetectPressure = true,
                    canShowEnableStylusDialog = true,
                    canShowDisableStylusDialog = true
                ),
                isEraserActive = true,
                eraserRadius = 0.6f,
            )
        }

    }

    @Test
    fun testColorPicker() = runComposeUiTest {

        setContent {
            ColorPicker(
                onDismissRequest = {},
                defaultColor = Color.RED,
                onClick = {},
                onColorPaletteClick = {},
                artMakerConfiguration = ArtMakerConfiguration(),
            )
        }

        onNodeWithTag(testTag = "Color Picker Modal Bottom Sheet").assertExists()

    }

    @Test
    fun testStrokePreview() = runComposeUiTest {

        setContent {
            StrokePreview(state = StrokeState(strokeColor = Color.RED, strokeWidth = 6, lineStyle = LineStyle.DASHED))
        }

        onNodeWithTag(testTag = "Stroke Preview Box").assertExists()
        onNodeWithTag(testTag = "Stroke Preview Canvas").assertExists()

    }

    @Test
    fun testSlider() = runComposeUiTest {

        setContent {
            Slider(sliderPosition = 0.6f, onValueChange = {}, configuration = ArtMakerConfiguration())
        }

        onNodeWithText(text = "Set Width Text").assertIsDisplayed()
        onNodeWithTag(testTag = "Slider").assertExists()

    }

    @Test
    fun testCustomColorPalette() = runComposeUiTest {

        setContent {
            CustomColorPalette(onAccept = {}, onCancel = {})
        }

        onNodeWithTag(testTag = "Saturation Value Area").assertExists()
        onNodeWithTag(testTag = "Hue Bar").assertExists()

    }

}