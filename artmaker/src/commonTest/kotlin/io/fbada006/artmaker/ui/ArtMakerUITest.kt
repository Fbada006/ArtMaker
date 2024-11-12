package io.fbada006.artmaker.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.runComposeUiTest
import io.fbada006.artmaker.ArtMakerUIState
import io.fbada006.artmaker.composables.ArtMakerControlMenu
import io.fbada006.artmaker.models.ArtMakerConfiguration
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
                isEraserActive = true
            )
        }

        onNodeWithContentDescription(label = "Color Picker Icon").assertExists()
        onNodeWithContentDescription(label = "Edit Icon").assertExists()
        onNodeWithContentDescription(label = "Ink Eraser Icon").assertExists()
        onNodeWithContentDescription(label = "Undo Icon").assertExists()
        onNodeWithContentDescription(label = "Redo Icon").assertExists()
        onNodeWithContentDescription(label = "Refresh Icon").assertExists()
        onNodeWithContentDescription(label = "Image Selector Icon").assertExists()

    }

    @Test
    fun testArtMakerDrawScreen() = runComposeUiTest {  }

    @Test
    fun testColorPicker() = runComposeUiTest {  }

    @Test
    fun testStrokePreview() = runComposeUiTest {  }

    @Test
    fun testSlider() = runComposeUiTest {  }

    @Test
    fun testCustomColorPalette() = runComposeUiTest {  }

}