package com.yeocak.parallaximage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.DrawableRes
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