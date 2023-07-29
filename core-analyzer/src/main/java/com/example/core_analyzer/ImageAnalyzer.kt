package com.example.core_analyzer

import android.graphics.Bitmap
import com.example.core_database.entities.Image
import com.example.core_database.entities.ImageWithObjects

interface ImageAnalyzer {

    suspend fun analyzeImage(image: Image, bitmap: Bitmap): ImageWithObjects
}