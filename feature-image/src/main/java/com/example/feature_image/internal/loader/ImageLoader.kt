package com.example.feature_image.internal.loader

import android.content.Context
import android.graphics.Bitmap
import com.example.core_extensions.getImageFromExternalStorageByIdSync
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ImageLoader @Inject constructor(private val context: Context) {

    suspend fun loadImage(imageId: Long): Bitmap? = withContext(Dispatchers.IO) {
        context.getImageFromExternalStorageByIdSync(imageId)
    }
}