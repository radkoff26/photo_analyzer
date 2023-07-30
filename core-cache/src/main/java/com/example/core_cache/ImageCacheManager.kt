package com.example.core_cache

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

interface ImageCacheManager {

    fun loadImage(imageId: Long): Flow<Bitmap?>

    fun onClear()
}