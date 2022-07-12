package com.yeocak.composableparallaximage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.yeocak.parallaximage.GravitySensorDefaulted
import com.yeocak.parallaximage.ParallaxImage

class MainActivity : ComponentActivity() {
    private lateinit var gravitySensorDefaulted: GravitySensorDefaulted

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gravitySensorDefaulted = GravitySensorDefaulted(this)

        setContent {
            Surface(
                modifier = Modifier.wrapContentSize()
            ) {
                ParallaxEffectImages()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        gravitySensorDefaulted.start()
    }

    override fun onStop() {
        super.onStop()
        gravitySensorDefaulted.stop()
    }

    @Composable
    private fun ParallaxEffectImages() {
        Column {
            ParallaxImage(
                image = R.drawable.test_photo,
                modifier = Modifier.weight(1f),
                sensor = gravitySensorDefaulted
            )
            ParallaxImage(
                image = R.drawable.test_photo,
                modifier = Modifier.weight(1f),
                sensor = gravitySensorDefaulted
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DefaultPreview() {
    val context = LocalContext.current
    val testSensor = remember {
        GravitySensorDefaulted(context).also {
            it.start()
        }
    }

    Column {
        ParallaxImage(
            image = R.drawable.test_photo,
            modifier = Modifier.weight(1f),
            sensor = testSensor
        )
        ParallaxImage(
            image = R.drawable.test_photo,
            modifier = Modifier.weight(1f),
            sensor = testSensor
        )
    }
}