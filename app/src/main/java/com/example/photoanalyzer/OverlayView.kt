package com.example.photoanalyzer

/*
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import org.tensorflow.lite.task.gms.vision.detector.Detection
import java.util.*
import kotlin.math.max

class OverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var results: List<Detection> = LinkedList<Detection>()
    private var boxPaint = Paint()
    private var textBackgroundPaint = Paint()
    private var textPaint = Paint()

    private var scaleFactorX: Float = 1f
    private var scaleFactorY: Float = 1f

    private var bounds = Rect()

    private var left: Int = 0
    private var top: Int = 0

    private var imageViewportWidth: Float = 0F
    private var imageViewportHeight: Float = 0F

    init {
        initPaints()
    }

    fun clear() {
        textPaint.reset()
        textBackgroundPaint.reset()
        boxPaint.reset()
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        textBackgroundPaint.color = Color.BLACK
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.textSize = 50f

        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 50f

        boxPaint.color = Color.GREEN
        boxPaint.strokeWidth = 8F
        boxPaint.style = Paint.Style.STROKE
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        for (result in results) {
            val boundingBox = result.boundingBox

            val top = this.top + boundingBox.top * scaleFactorY
            val bottom = this.top + boundingBox.bottom * scaleFactorY
            val left = this.left + boundingBox.left * scaleFactorX
            val right = this.left + boundingBox.right * scaleFactorX

            // Draw bounding box around detected objects
            val drawableRect = RectF(left, top, right, bottom)
            canvas.drawRect(drawableRect, boxPaint)

            // Create text to display alongside detected objects
            val drawableText =
                result.categories[0].label + " " + String.format("%.2f", result.categories[0].score)

            // Draw rect behind display text
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

            // Draw text for detected object
            canvas.drawText(drawableText, left, top + bounds.height(), textPaint)
        }
    }

    fun setResults(
        detectionResults: MutableList<Detection>,
        imageHeight: Int,
        imageWidth: Int
    ) {
        results = detectionResults

        val imageAspectRatio = imageWidth.toFloat() / imageHeight.toFloat()
        val overlayAspectRatio = width.toFloat() / height.toFloat()

        if (imageAspectRatio > overlayAspectRatio) {
            // Image is stretched by width
            imageViewportWidth = width.toFloat()
            imageViewportHeight = imageViewportWidth / imageAspectRatio
        } else {
            // Image is stretched by height or fills the overlay viewport
            imageViewportHeight = height.toFloat()
            imageViewportWidth = imageViewportHeight * imageAspectRatio
        }

        left = ((width.toFloat() - imageViewportWidth) / 2).toInt()
        top = ((height.toFloat() - imageViewportHeight) / 2).toInt()

        scaleFactorX = imageViewportWidth / imageWidth
        scaleFactorY = imageViewportHeight / imageHeight

        invalidate()
    }

    companion object {
        private const val BOUNDING_RECT_TEXT_PADDING = 8
    }
}*/
