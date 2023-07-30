package com.example.core_ai

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.gms.vision.TfLiteVision
import org.tensorflow.lite.task.gms.vision.detector.Detection
import org.tensorflow.lite.task.gms.vision.detector.ObjectDetector

class ObjectDetectorHelper(
    private var threshold: Float = 0.5f,
    private var numThreads: Int = 2,
    private var maxResults: Int = 5,
    private val context: Context
) {

    init {
        if (!TfLiteVision.isInitialized()) {
            TfLiteVision.initialize(context)
        }
    }

    private var objectDetector: ObjectDetector? = null

    private fun setupObjectDetector() {
        if (!TfLiteVision.isInitialized()) {
            Log.e(TAG, "setupObjectDetector: TfLiteVision is not initialized yet")
            return
        }

        val optionsBuilder =
            ObjectDetector.ObjectDetectorOptions.builder()
                .setScoreThreshold(threshold)
                .setMaxResults(maxResults)

        optionsBuilder.setBaseOptions(
            BaseOptions.builder()
                .setNumThreads(numThreads)
                .build()
        )

        try {
            objectDetector =
                ObjectDetector.createFromFileAndOptions(
                    context,
                    "mobilenetv1.tflite",
                    optionsBuilder.build()
                )
        } catch (e: Exception) {
            Log.e(TAG, "TFLite failed to load model with error: " + e.message, e)
        }
    }

    fun detectObjectsOnImage(image: Bitmap): DetectionResult? {
        if (!TfLiteVision.isInitialized()) {
            Log.e(TAG, "detect: TfLiteVision is not initialized yet")
            return null
        }

        if (objectDetector == null) {
            setupObjectDetector()
        }

        val imageProcessor = ImageProcessor.Builder().build()

        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(image))

        val results = objectDetector?.detect(tensorImage) ?: return null

        return DetectionResult(
            results,
            tensorImage.height,
            tensorImage.width
        )
    }

    data class DetectionResult(
        val results: List<Detection>,
        val imageHeight: Int,
        val imageWidth: Int
    )

    companion object {
        const val TAG = "ObjectDetectionHelperClass"
    }
}