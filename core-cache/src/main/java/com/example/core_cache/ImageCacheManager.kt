package com.example.core_cache

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.example.core_extensions.getImageFromExternalStorageById
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ImageCacheManager(private var context: Context?) {
    private val memoryLimitInKB = (Runtime.getRuntime().maxMemory() / 1024 / 1.5).toInt()
    private val imagesMap = object : LruCache<Long, Bitmap>(memoryLimitInKB) {
        override fun sizeOf(key: Long?, value: Bitmap?): Int {
            return if (value == null) 0 else value.byteCount / 1024
        }
    }

    fun loadImage(imageId: Long): Flow<Bitmap?> {
        return flow {
            val containedValue = imagesMap[imageId]
            if (containedValue == null) {
                emit(null)
                context?.getImageFromExternalStorageById(imageId)?.collect {
                    if (it != null) {
                        imagesMap.put(imageId, it)
                    }
                    emit(it)
                }
            } else {
                emit(containedValue)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun onClear() {
        context = null
    }
}