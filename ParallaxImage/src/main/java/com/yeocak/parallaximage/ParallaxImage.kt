package com.yeocak.parallaximage

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ParallaxImage(
    @DrawableRes image: Int,
    modifier: Modifier = Modifier,
    sensor: GravitySensorDefaulted,
    imagePadding: Dp = 70.dp,
    imageCornerRadius: Dp = 10.dp,
    blurredImageDistance: Dp = 10.dp,
    transparentColor: Color = Color.Black
) {
    val context = LocalContext.current

    val positions = remember { mutableStateOf(Triple(0f, 0f, 0f)) }

    LaunchedEffect(null) {
        sensor.addOnChangeListener { x, y, z ->
            positions.value = Triple(x, y, z)
        }
    }

    val normalBitmap = remember { context.getBitmap(image) }
    val blurredBitmap = remember { context.blurImage(image, 1f, 15f) }

    Box(
        modifier = modifier
    ) {
        // Back blurred image
        Image(
            bitmap = blurredBitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .offset((positions.value.first * 8).dp, -(positions.value.second * 10).dp)
                .padding(imagePadding + blurredImageDistance)
                .clip(RoundedCornerShape(imageCornerRadius))
                .graphicsLayer { alpha = 0.99f }
                .drawWithContent { blurEdges(transparentColor, 0.9f) }
        )

        // Front original image
        Image(
            bitmap = normalBitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(imagePadding)
                .clip(RoundedCornerShape(imageCornerRadius))
        )
    }
}