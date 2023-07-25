package com.example.core_design

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import java.lang.Float.min

class RoundedImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): AppCompatImageView(context, attrs, defStyleAttr) {
    private val rectF = RectF()
    private val path = Path()

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        rectF.left = left.toFloat()
        rectF.top = top.toFloat()
        rectF.right = right.toFloat()
        rectF.bottom = bottom.toFloat()
        val radius = min(rectF.width(), rectF.height()) * STROKE_RADIUS_FACTOR
        path.addRoundRect(rectF, radius, radius, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.clipPath(path)
        super.onDraw(canvas)
    }

    companion object {
        private const val STROKE_RADIUS_FACTOR = 0.1f
    }
}