## Getting started

## Download

### Gradle
Add the dependency below to your **module**'s `build.gradle` file:
```gradle
dependencies {
    implementation ""
}
```

Find latest version and release notes [here](https://github.com/Fbada006/ArtMaker/releases)

## Usage

**ArtMaker** implements canvas, which allows you to draw points with custom properties.
You can use the `ArtMaker` with default configuration as the following example:

```kotlin
ArtMaker(
    modifier = Modifier.fillMaxSize(),
)
```

Alternatively you can have full control of the ArtMaker and choose how you want the control menu composable (the bar below the drawing area) to be
displayed by customizing the `ArtMakerConfiguration` object as shown below:

```kotlin
ArtMaker(
    modifier = Modifier.fillMaxSize(),
    artMakerConfiguration = ArtMakerConfiguration(
        strokeSliderThumbColor = MaterialTheme.colorScheme.primary,
        strokeSliderActiveTrackColor = MaterialTheme.colorScheme.onPrimary,
        strokeSliderInactiveTickColor = MaterialTheme.colorScheme.inversePrimary,
        strokeSliderTextColor = MaterialTheme.colorScheme.secondaryContainer,
        pickerCustomColors = listOf(
            Color.Red, Color.Blue, Color.Green,
        ),
        canvasBackgroundColor = Color.Green.toArgb(),
        controllerBackgroundColor = Color.Cyan,
        strokeSliderBackgroundColor = Color.Magenta,
    )
)
```

## Contribute

Do you see any improvements or want to implement a missing feature? Contributions are very welcome!

- Is your contribution relatively small? You can, make your changes, run the code checks, open a PR
  and make sure the CI is green. That's it!
- Are the changes big and do they make a lot of impact? Please open an
  issue [here](https://github.com/Fbada006/ArtMaker/issues?q=is%3Aissue) or reach out to [Ferdinand](https://github.com/Fbada006), [Emmanuel](https://github.com/emmanuelmuturia) or [Caleb](https://github.com/CalebKL) and
  let's discuss.
- Ensure your change is properly formatted by running the following command from the terminal:

```gradle
./gradlew spotlessApply
```

The CI will fail if the code is not properly formatted. Please correct any failures before requesting a review.

Take into account that changes and requests can be rejected if they don't align with the **purpose
of the library**. To ensure you do not waste any time, you can always open an
issue [here](https://github.com/Fbada006/ArtMaker/issues?q=is%3Aissue) or talk to us before you
start making any changes.

## License

