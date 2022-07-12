package com.yeocak.parallaximage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import kotlin.math.roundToInt

private const val BITMAP_SCALE = 0.4f
private const val BLUR_RADIUS = 7.5f

internal fun Context.getBitmap(@DrawableRes image: Int) = BitmapFactory.decodeResource(
    resources,
    image
)

internal fun Context.blurImage(
    @DrawableRes image: Int,
    bitmapScale: Float = BITMAP_SCALE,
    blurRadius: Float = BLUR_RADIUS
): Bitmap = blurImage(getBitmap(image), bitmapScale, blurRadius)

internal fun Context.blurImage(
    image: Bitmap,
    bitmapScale: Float = BITMAP_SCALE,
    blurRadius: Float = BLUR_RADIUS
): Bitmap {
    val newWidth = (image.width * bitmapScale).roundToInt()
    val newHeight = (image.height * bitmapScale).roundToInt()

    val inputBitmap = Bitmap.createScaledBitmap(image, newWidth, newHeight, false)
    val outputBitmap = Bitmap.createBitmap(inputBitmap)
    val rs: RenderScript = RenderScript.create(this)
    val theIntrinsic: ScriptIntrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
    val tmpIn: Allocation = Allocation.createFromBitmap(rs, inputBitmap)
    val tmpOut: Allocation = Allocation.createFromBitmap(rs, outputBitmap)

    theIntrinsic.setRadius(blurRadius)
    theIntrinsic.setInput(tmpIn)
    theIntrinsic.forEach(tmpOut)
    tmpOut.copyTo(outputBitmap)

    return Bitmap.createScaledBitmap(
        outputBitmap, image.width, image.height, false
    )
}

internal fun ContentDrawScope.blurEdges(
    transparentColor: Color,
    @FloatRange(from = 0.0, to = 1.0) blurEdgeRatio: Float
) {
    val fadeSideToRight = listOf(transparentColor, Color.Transparent)
    val fadeSideToLeft = fadeSideToRight.reversed()

    drawContent()
    drawRect(
        brush = Brush.horizontalGradient(
            fadeSideToRight,
            this.size.width * blurEdgeRatio,
            this.size.width
        ),
        blendMode = BlendMode.DstIn
    )
    drawRect(
        brush = Brush.horizontalGradient(
            fadeSideToLeft,
            0f,
            this.size.width * (1 - blurEdgeRatio)
        ),
        blendMode = BlendMode.DstIn
    )
    drawRect(
        brush = Brush.verticalGradient(
            fadeSideToLeft,
            0f,
            this.size.height * (1 - blurEdgeRatio)
        ),
        blendMode = BlendMode.DstIn
    )
    drawRect(
        brush = Brush.verticalGradient(
            fadeSideToRight,
            this.size.height * blurEdgeRatio,
            this.size.height
        ),
        blendMode = BlendMode.DstIn
    )
}