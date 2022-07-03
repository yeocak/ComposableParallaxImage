package com.yeocak.parallaximage

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ParallaxImage(
    @DrawableRes image: Int,
    modifier: Modifier = Modifier,
    imagePadding: Dp
) {
    val context = LocalContext.current

    val positions = remember { mutableStateOf(Triple(0f,0f,0f)) }

    remember {
        SensorEvents(context).also { sensor ->
            sensor.setOnChangeListener { x, y, z ->
                positions.value = Triple(x,y,z)
            }
            sensor.start()
        }
    }

    val normalBitmap = remember { context.getBitmap(image) }
    val blurredBitmap = remember { context.blurImage(image, 1f, 15f) }

    val fadeSideToRight = remember { listOf(Color.Black, Color.Transparent) }
    val fadeSideToLeft = remember { listOf(Color.Transparent, Color.Black) }

    Box(
        modifier = modifier
    ) {
        Image(
            bitmap = blurredBitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .offset((positions.value.first * 8).dp, -(positions.value.second * 8).dp)
                .padding(imagePadding + 40.dp)
                .clip(RoundedCornerShape(10.dp))
                .graphicsLayer { alpha = 0.99f }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.horizontalGradient(fadeSideToRight, this.size.width * 9f / 10f, this.size.width),
                        blendMode = BlendMode.DstIn
                    )
                    drawRect(
                        brush = Brush.horizontalGradient(fadeSideToLeft, 0f, this.size.width * 1f / 10f),
                        blendMode = BlendMode.DstIn
                    )
                    drawRect(
                        brush = Brush.verticalGradient(fadeSideToLeft, 0f, this.size.height * 1f / 10f),
                        blendMode = BlendMode.DstIn
                    )
                    drawRect(
                        brush = Brush.verticalGradient(fadeSideToRight, this.size.height * 9f / 10f, this.size.height),
                        blendMode = BlendMode.DstIn
                    )
                }
        )
        Image(
            bitmap = normalBitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(imagePadding + 30.dp)
                .clip(RoundedCornerShape(10.dp))
        )
    }
}