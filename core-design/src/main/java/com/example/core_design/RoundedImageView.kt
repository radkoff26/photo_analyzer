package com.example.core_design

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
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

    // Цвет для отдельной отрисовки background'а (закруглённого)
    // Он будет отрисовываться отдельно в onDraw
    private val backgroundPaint: Paint?

    var radius = 0F
        private set

    init {
        // Не устанавливаем цвет краски background'а, если цвет background'а не уставновлен на View
        if (backgroundTintList?.defaultColor == null) {
            backgroundPaint = null
        } else {
            backgroundPaint = Paint().apply {
                color = backgroundTintList!!.defaultColor
            }
        }
        // Обнуляем background, дабы он автоматически не рисовался не закруглённым
        // Делегируем отрисовку background'а именно отдельно на onDraw
        backgroundTintList = null
        background = null
    }

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
        canvas?.clipPath(path)
        // Если цвет background'а не установлен, то background отрисовываться не будет
        if (backgroundPaint != null) {
            canvas?.drawRoundRect(rectF, radius, radius, backgroundPaint)
        }
        super.onDraw(canvas)
    }

    companion object {
        private const val CORNER_RADIUS_FACTOR = 0.1f
    }
}