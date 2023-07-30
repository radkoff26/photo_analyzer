package com.example.feature_image.internal.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.core_database.entities.ObjectOnImage

internal class OverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var results: Map<String, ObjectOnImage> = emptyMap()
    private var boxPaint = Paint()
    private var spottedBoxPaint = Paint()
    private var textBackgroundPaint = Paint()
    private var textPaint = Paint()

    private var scaleFactorX: Float = 1f
    private var scaleFactorY: Float = 1f

    private var bounds = Rect()

    private var left: Int = 0
    private var top: Int = 0

    private var imageViewportWidth: Float = 0F
    private var imageViewportHeight: Float = 0F

    private var spottedObjectKey: String? = null

    init {
        initPaints()
    }

    private fun initPaints() {
        textBackgroundPaint.color = Color.BLACK
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.textSize = 50f

        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 50f

        spottedBoxPaint.color = Color.GREEN
        spottedBoxPaint.strokeWidth = 8F
        spottedBoxPaint.style = Paint.Style.STROKE

        boxPaint.color = Color.LTGRAY
        boxPaint.strokeWidth = 8F
        boxPaint.style = Paint.Style.STROKE
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        for (result in results) {
            val currentObject = result.value
            val top = this.top + currentObject.top * scaleFactorY
            val bottom = this.top + currentObject.bottom * scaleFactorY
            val left = this.left + currentObject.left * scaleFactorX
            val right = this.left + currentObject.right * scaleFactorX

            val paintForBox = if (result.key == spottedObjectKey) {
                spottedBoxPaint
            } else {
                boxPaint
            }

            val drawableRect = RectF(left, top, right, bottom)
            canvas.drawRect(drawableRect, paintForBox)

            if (result.key == spottedObjectKey) {
                val drawableText = result.key

                textBackgroundPaint.getTextBounds(drawableText, 0, drawableText.length, bounds)
                val textWidth = bounds.width()
                val textHeight = bounds.height()
                canvas.drawRect(
                    left,
                    top,
                    left + textWidth + BOUNDING_RECT_TEXT_PADDING,
                    top + textHeight + BOUNDING_RECT_TEXT_PADDING,
                    textBackgroundPaint
                )

                canvas.drawText(drawableText, left, top + bounds.height(), textPaint)
            }
        }
    }

    fun setResults(
        detectionResults: Map<String, ObjectOnImage>,
        imageHeight: Int,
        imageWidth: Int
    ) {
        results = detectionResults

        updateDimensions(imageHeight, imageWidth)

        invalidate()
    }

    fun spotObject(key: String) {
        spottedObjectKey = key

        invalidate()
    }

    private fun updateDimensions(
        imageHeight: Int,
        imageWidth: Int
    ) {
        val imageAspectRatio = imageWidth.toFloat() / imageHeight.toFloat()
        val overlayAspectRatio = width.toFloat() / height.toFloat()

        if (imageAspectRatio > overlayAspectRatio) {
            imageViewportWidth = width.toFloat()
            imageViewportHeight = imageViewportWidth / imageAspectRatio
        } else {
            imageViewportHeight = height.toFloat()
            imageViewportWidth = imageViewportHeight * imageAspectRatio
        }

        left = ((width.toFloat() - imageViewportWidth) / 2).toInt()
        top = ((height.toFloat() - imageViewportHeight) / 2).toInt()

        scaleFactorX = imageViewportWidth / imageWidth
        scaleFactorY = imageViewportHeight / imageHeight
    }

    companion object {
        private const val BOUNDING_RECT_TEXT_PADDING = 8
    }
}
