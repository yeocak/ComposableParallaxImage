package com.yeocak.composableparallaximage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yeocak.composableparallaximage.ui.theme.ComposableParallaxImageTheme
import com.yeocak.parallaximage.ParallaxImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposableParallaxImageTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    ParallaxImage(R.drawable.test_photo, Modifier.fillMaxSize(), 30.dp)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposableParallaxImageTheme {
        Greeting()
    }
}