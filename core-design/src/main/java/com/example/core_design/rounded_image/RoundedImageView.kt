package com.example.core_design.rounded_image

import android.content.Context
import android.graphics.Bitmap
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
) : AppCompatImageView(context, attrs, defStyleAttr) {
    private val rectF = RectF()
    private val path = Path()

    private var onImageChangedListener: OnImageChangedListener? = null

    var radius = 0F
        private set

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        rectF.left = left.toFloat()
        rectF.top = top.toFloat()
        rectF.right = right.toFloat()
        rectF.bottom = bottom.toFloat()
        radius = min(rectF.width(), rectF.height()) * CORNER_RADIUS_FACTOR
        path.addRoundRect(rectF, radius, radius, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas?) {
        // Ограничиваем View закруглённым контуром
        canvas?.clipPath(path)
        super.onDraw(canvas)
    }

    override fun setImageBitmap(bm: Bitmap?) {
        if (bm == null) {
            // Если bitmap - null, то это значит, что картинка удалена с View
            // В таком случае, вызываем callback
            onImageChangedListener?.onImageRemoved()
            // Затем устанавливаем nullable bitmap
            super.setImageBitmap(null)
        } else {
            // Если же bitmap - не null, то это означает, что картинка устанавливается
            // Устанавливаем сам bitmap
            super.setImageBitmap(bm)
            // Вызываем callback
            onImageChangedListener?.onImageSet()
        }
    }

    fun setOnImageSettledListener(onImageChangedListener: OnImageChangedListener) {
        this.onImageChangedListener = onImageChangedListener
    }

    companion object {
        private const val CORNER_RADIUS_FACTOR = 0.1f
    }
}