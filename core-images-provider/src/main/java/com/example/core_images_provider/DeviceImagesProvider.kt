package com.example.core_images_provider

import android.content.Context
import android.provider.MediaStore.Images.Media
import com.example.core_database.entities.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.min

class DeviceImagesProvider(private val context: Context) : ImagesProvider {
    private val contentUri = Media.EXTERNAL_CONTENT_URI
    private val cursor by lazy {
        context.contentResolver.query(
            contentUri,
            arrayOf(Media._ID, Media.DATE_MODIFIED),
            null,
            null,
            "${Media.DATE_ADDED} DESC"
        )!!
    }

    private val lock = Any()

    override suspend fun getPagedImages(currentKey: Int, pageSize: Int): List<Image> {
        return withContext(Dispatchers.IO) {
            var index = currentKey
            val end = min(cursor.count, currentKey + pageSize)

            if (index >= cursor.count) {
                return@withContext emptyList()
            }

            val imagesList: MutableList<Image> = ArrayList()

            val imageIdColumnIndex = cursor.getColumnIndex(Media._ID)
            val imageModificationDateColumnIndex = cursor.getColumnIndex(Media.DATE_MODIFIED)

            synchronized(lock) {
                while (index < end) {
                    cursor.moveToPosition(index)

                    val imageId = cursor.getLong(imageIdColumnIndex)
                    val imageModificationDate = cursor.getLong(imageModificationDateColumnIndex)

                    imagesList.add(
                        Image(
                            imageId,
                            java.sql.Timestamp(imageModificationDate * 1000),
                            0,
                            0
                        )
                    )

                    index++
                }
            }

            return@withContext imagesList
        }
    }
}