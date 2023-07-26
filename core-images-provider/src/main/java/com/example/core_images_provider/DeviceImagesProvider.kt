package com.example.core_images_provider

import android.content.Context
import android.provider.MediaStore.Images.Media
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.math.min

class DeviceImagesProvider(private val context: Context) : ImagesProvider {
    private val contentUri = Media.EXTERNAL_CONTENT_URI
    private val cursor by lazy {
        context.contentResolver.query(
            contentUri,
            arrayOf(Media._ID),
            null,
            null,
            "${Media.DATE_ADDED} DESC"
        )!!
    }

    private val lock = Any()

    @Volatile
    private var currentPosition = 0

    override fun getNextNPhotos(n: Int): Flow<List<Long>?> {
        return flow {
            if (currentPosition >= cursor.count) {
                emit(null)
            }

            val imagesList: MutableList<Long> = ArrayList()

            val imageIdColumnIndex = cursor.getColumnIndex(Media._ID)

            synchronized(lock) {
                val bound = min(cursor.count, currentPosition + n)

                while (currentPosition < bound) {
                    cursor.moveToPosition(currentPosition)

                    val imageId = cursor.getLong(imageIdColumnIndex)

                    imagesList.add(imageId)

                    currentPosition++
                }
            }

            emit(imagesList)
        }.flowOn(Dispatchers.IO)
    }
}