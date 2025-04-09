package com.codestracture.utils.ext

import android.content.Context
// import android.graphics.Canvas
// import android.graphics.Paint
// import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
// import android.graphics.drawable.ShapeDrawable
// import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
// import com.bumptech.glide.Glide
// import com.joyride.common.utility.VectorColorFilterDrawable
// import com.pixplicity.sharp.OnSvgElementListener
// import com.pixplicity.sharp.Sharp

fun Context.getGradientDrawable(firstColor: Int, secondColor: Int): GradientDrawable {
    val scale = resources.displayMetrics.density
    val pixels = (10 * scale + 0.5f)
    val gd = GradientDrawable(
        GradientDrawable.Orientation.LEFT_RIGHT,
        intArrayOf(firstColor, secondColor)
    ).apply { cornerRadius = pixels }
    return gd
}

fun Context.getGradientDrawableWith5dp(firstColor: Int, secondColor: Int): GradientDrawable {
    val scale = resources.displayMetrics.density
    val pixels = (7 * scale + 0.5f)
    val gd = GradientDrawable(
        GradientDrawable.Orientation.LEFT_RIGHT,
        intArrayOf(firstColor, secondColor)
    ).apply { cornerRadius = pixels }
    return gd
}

fun Context.getGradientDrawableWith3dp(firstColor: Int, secondColor: Int): GradientDrawable {
    val scale = resources.displayMetrics.density
    val pixels = (4 * scale + 0.5f)
    val gd = GradientDrawable(
        GradientDrawable.Orientation.LEFT_RIGHT,
        intArrayOf(firstColor, secondColor)
    ).apply { cornerRadius = pixels }
    return gd
}

fun Context.getGradientDrawableWithStroke(
    firstColor: Int,
    secondColor: Int
): GradientDrawable {
    val scale = resources.displayMetrics.density
    val pixels = (25 * scale + 0.5f)
    val shape = GradientDrawable()
    shape.shape = GradientDrawable.RECTANGLE
    shape.cornerRadius = pixels
    shape.setColor(firstColor)
    shape.setStroke(4, secondColor)
    return shape
}

//  ̉fun Context.getDrawablePathColorFilter(resId: Int, oldColor: Int, firstColor: Int): Drawable {
//    val originalDrawable = ContextCompat.getDrawable(this, resId)
//    val vectorShape = VectorColorFilterDrawable(this, resId, oldColor, firstColor)
//    val shapeDrawable = ShapeDrawable(vectorShape).apply {
//        intrinsicWidth = originalDrawable!!.intrinsicWidth
//        intrinsicHeight = originalDrawable.intrinsicHeight
//    }
//    return shapeDrawable
// }
//
// fun Context.getDrawablePathColorFilterSvg(resId: Int, oldColor: Int, firstColor: Int): Drawable {
//    return Sharp.loadResource(resources, resId).apply {
//      setOnElementListener(object : OnSvgElementListener {
//          override fun onSvgStart(canvas: Canvas, bounds: RectF?) { }
//
//          override fun onSvgEnd(canvas: Canvas, bounds: RectF?) { }
//
//          override fun <T : Any> onSvgElement(
//              id: String?,
//              element: T,
//              elementBounds: RectF?,
//              canvas: Canvas,
//              canvasBounds: RectF?,
//              paint: Paint?
//          ): T {
//                if (paint?.color == oldColor) {
//                    paint.color = firstColor
//                }
//            return element
//          }
//
//          override fun <T : Any> onSvgElementDrawn(id: String?, element: T,
//              canvas: Canvas, paint: Paint?) { }
//      })
//    }.drawable
// }

fun Context.getDrawableShapeColorFilter(resId: Int, shapeId: Int, firstColor: Int): Drawable {
    val layerDrawable = ContextCompat.getDrawable(this, resId) as LayerDrawable
    (layerDrawable.findDrawableByLayerId(shapeId) as GradientDrawable).setColor(firstColor)
    return layerDrawable
}

// fun AppCompatImageView.loadUrlImage(imageUrl:String?){
//    this.background=null
//    Glide.with(this.context).load(imageUrl)
//        .into(this)
// }
//
// fun AppCompatImageView.loadUrlImageWithCompress(imageUrl: String?) {
//    this.background = null
//    Glide.with(this.context).load(imageUrl)
//        .into(this)
// }
