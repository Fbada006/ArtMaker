package com.artmaker.artmaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.artmaker.ArtMaker
import com.artmaker.artmaker.ui.theme.ArtMakerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArtMakerTheme {
                ArtMaker()
            }
        }
    }
}
