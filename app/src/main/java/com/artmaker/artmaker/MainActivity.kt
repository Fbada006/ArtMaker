package com.artmaker.artmaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.artmaker.ArtMaker
import com.artmaker.artmaker.ui.theme.ArtMakerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtMakerTheme {
                ArtMaker(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
