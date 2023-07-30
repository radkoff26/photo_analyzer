package com.example.core_analyzer

import android.graphics.Bitmap
import com.example.core_ai.ObjectDetectorHelper
import com.example.core_database.entities.Image
import com.example.core_database.entities.ImageWithObjects
import com.example.core_database.entities.ObjectOnImage
import com.example.core_database.entities.ObjectOnImageType

class ImageAnalyzerImpl constructor(
    private val objectDetectorHelper: ObjectDetectorHelper
) : ImageAnalyzer {

    override suspend fun analyzeImage(image: Image, bitmap: Bitmap): ImageWithObjects {
        val detectionResult = objectDetectorHelper.detectObjectsOnImage(bitmap)
            ?: return ImageWithObjects(image, emptyList())

        val objectsOnImage = detectionResult.results
            .filter {
                try {
                    ObjectOnImageType.valueOf(it.categories.first().label.uppercase())
                    true
                } catch (e: Exception) {
                    false
                }
            }
            .map {
                val probableCategory = it.categories.first()
                val type = ObjectOnImageType.valueOf(probableCategory.label.uppercase())
                ObjectOnImage(
                    id = 0, // Auto-generated
                    imageId = image.id,
                    type = type,
                    left = it.boundingBox.left,
                    top = it.boundingBox.top,
                    right = it.boundingBox.right,
                    bottom = it.boundingBox.bottom
                )
            }

        return ImageWithObjects(
            image.copy(
                height = detectionResult.imageHeight,
                width = detectionResult.imageWidth,
                isProcessed = true
            ),
            objectsOnImage
        )
    }
}