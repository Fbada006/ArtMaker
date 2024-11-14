package io.fbada006.artmaker.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.swipeRight
import io.fbada006.artmaker.ArtMaker
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
        onNodeWithTag(testTag = "Control Menu Surface").assertExists()
        onNodeWithTag(testTag = "Control Menu Dropdown").assertExists()
        onNodeWithTag(testTag = "Control Menu Dropdown").performClick()
        onNodeWithText(text = "Change Image").assertIsDisplayed()
        onNodeWithText(text = "Clear Image").assertIsDisplayed()

    }

    @Test
    fun testArtMakerDrawScreen() = runComposeUiTest {

        var drawScreenState = DrawScreenState(
            pathList = mutableStateListOf(),
            backgroundImage = null,
            backgroundColor = Color.RED,
            shouldTriggerArtExport = true,
            isFullScreenMode = true,
            isStylusAvailable = true,
            shouldUseStylusOnly = true,
            shouldDetectPressure = true,
            canShowEnableStylusDialog = true,
            canShowDisableStylusDialog = true,
        )

        setContent {
            ArtMakerDrawScreen(
                configuration = ArtMakerConfiguration(),
                onDrawEvent = {},
                onAction = {},
                state = drawScreenState,
                isEraserActive = true,
                eraserRadius = 0.6f,
            )
        }

        onNodeWithTag(testTag = "Draw Screen Box").assertExists()
        drawScreenState = drawScreenState.copy(canShowEnableStylusDialog = true)
        onNodeWithTag(testTag = "Should Show Stylus Dialog").assertExists()
        // AlertDialog Title and Text go here...
        onNodeWithText(text = "Got it").assertExists()
        onNodeWithText(text = "Got it").performClick()
        onNodeWithTag(testTag = "Should Show Stylus Dialog").assertDoesNotExist()

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
        onNodeWithTag(testTag = "Color Picker Default Colours").assertExists()
        onNodeWithTag(testTag = "Color Picker Custom Colours").assertExists()
        // Test the Custom Colours and the Flow Row based on the condition here...
        onNodeWithTag(testTag = "Custom Color Picker").assertExists()
        onNodeWithTag(testTag = "Color Item").assertExists()
        onNodeWithTag(testTag = "Color Item").performClick()
        // Maybe try and test the checkmark icon...

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

        onNodeWithText(text = "Set Width: ${0.6f.toInt()}").assertIsDisplayed()
        onNodeWithTag(testTag = "Slider").assertExists()
        onNodeWithTag(testTag = "Slider").performTouchInput {
            swipeRight()
        }

    }

    @Test
    fun testCustomColorPalette() = runComposeUiTest {

        setContent {
            CustomColorPalette(onAccept = {}, onCancel = {})
        }

        onNodeWithTag(testTag = "Saturation Value Area").assertExists()
        onNodeWithTag(testTag = "Hue Bar").assertExists()
        onNodeWithText(text = "Cancel").assertIsDisplayed()
        onNodeWithText(text = "Cancel").performClick()
        onNodeWithText(text = "Ok").assertIsDisplayed()
        onNodeWithText(text = "Ok").performClick()

    }

    @Test
    fun testArtMaker() = runComposeUiTest {

        setContent {
            ArtMaker()
        }

        onNodeWithContentDescription(label = Icons.Filled.Share.name).assertIsDisplayed()
        onNodeWithContentDescription(label = Icons.Filled.Done.name).assertIsDisplayed()
        onNodeWithContentDescription(label = Icons.Filled.Share.name).assertIsDisplayed()

        onNodeWithContentDescription(label = Icons.Filled.FullscreenExit.name).assertIsDisplayed()
        onNodeWithContentDescription(label = Icons.Filled.FullscreenExit.name).performClick()
        onNodeWithContentDescription(label = Icons.Filled.Fullscreen.name).assertIsDisplayed()
        onNodeWithContentDescription(label = Icons.Filled.Fullscreen.name).performClick()
        onNodeWithContentDescription(label = Icons.Filled.FullscreenExit.name).assertIsDisplayed()


    }

}