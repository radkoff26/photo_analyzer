package com.example.core_images_provider

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore.Images.Media
import com.example.core_database.entities.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.min

class DeviceImagesProvider(private val context: Context) : ImagesProvider {
    private val contentUri = Media.EXTERNAL_CONTENT_URI
    private val pagedCursor by lazy {
        context.contentResolver.query(
            contentUri,
            IMAGE_PROJECTION,
            null,
            null,
            MODIFICATION_DATE_DESC_SORT_ORDER
        )!!
    }

    private val lock = Any()

    override suspend fun getPagedImages(currentKey: Int, pageSize: Int): List<Image> {
        return withContext(Dispatchers.IO) {
            var index = currentKey
            val end = min(pagedCursor.count, currentKey + pageSize)

            if (index >= pagedCursor.count) {
                return@withContext emptyList()
            }

            val imagesList: MutableList<Image> = ArrayList()

            val imageIdColumnIndex = pagedCursor.getColumnIndex(Media._ID)
            val imageModificationDateColumnIndex = pagedCursor.getColumnIndex(Media.DATE_MODIFIED)
            val imageHeightColumnIndex = pagedCursor.getColumnIndex(Media.HEIGHT)
            val imageWidthColumnIndex = pagedCursor.getColumnIndex(Media.WIDTH)

            synchronized(lock) {
                while (index < end) {
                    pagedCursor.moveToPosition(index)

                    val imageId = pagedCursor.getLong(imageIdColumnIndex)
                    val imageModificationDate =
                        pagedCursor.getLong(imageModificationDateColumnIndex)
                    val imageHeight = pagedCursor.getInt(imageHeightColumnIndex)
                    val imageWidth = pagedCursor.getInt(imageWidthColumnIndex)

                    imagesList.add(
                        Image(
                            imageId,
                            java.sql.Timestamp(imageModificationDate * 1000),
                            imageHeight,
                            imageWidth
                        )
                    )

                    index++
                }
            }

            return@withContext imagesList
        }
    }

    override suspend fun getImageById(id: Long): Image? {
        val uri = ContentUris.withAppendedId(contentUri, id)
        context.contentResolver.query(
            uri,
            IMAGE_PROJECTION,
            null,
            null,
            MODIFICATION_DATE_DESC_SORT_ORDER
        ).use {
            if (it == null || it.count == 0) {
                return null
            }

            it.moveToFirst()

            val imageIdColumnIndex = it.getColumnIndex(Media._ID)
            val imageModificationDateColumnIndex = it.getColumnIndex(Media.DATE_MODIFIED)
            val imageHeightColumnIndex = it.getColumnIndex(Media.HEIGHT)
            val imageWidthColumnIndex = it.getColumnIndex(Media.WIDTH)

            val imageId = it.getLong(imageIdColumnIndex)
            val imageModificationDate = it.getLong(imageModificationDateColumnIndex)
            val imageHeight = it.getInt(imageHeightColumnIndex)
            val imageWidth = it.getInt(imageWidthColumnIndex)

            return Image(
                imageId,
                java.sql.Timestamp(imageModificationDate * 1000),
                imageHeight,
                imageWidth
            )
        }
    }

    override fun getAllImagesSync(): List<Image> =
        context.contentResolver.query(
            contentUri,
            IMAGE_PROJECTION,
            null,
            null,
            MODIFICATION_DATE_DESC_SORT_ORDER
        )!!.use {
            var index = 0
            val imagesList: MutableList<Image> = ArrayList()

            val imageIdColumnIndex = it.getColumnIndex(Media._ID)
            val imageModificationDateColumnIndex = it.getColumnIndex(Media.DATE_MODIFIED)
            val imageHeightColumnIndex = it.getColumnIndex(Media.HEIGHT)
            val imageWidthColumnIndex = it.getColumnIndex(Media.WIDTH)

            while (index < it.count) {
                it.moveToPosition(index)

                val imageId = it.getLong(imageIdColumnIndex)
                val imageModificationDate = it.getLong(imageModificationDateColumnIndex)
                val imageHeight = it.getInt(imageHeightColumnIndex)
                val imageWidth = it.getInt(imageWidthColumnIndex)

                imagesList.add(
                    Image(
                        imageId,
                        java.sql.Timestamp(imageModificationDate * 1000),
                        imageHeight,
                        imageWidth
                    )
                )

                index++
            }

            it.close()

            return imagesList
        }

    companion object {
        private const val MODIFICATION_DATE_DESC_SORT_ORDER = "${Media.DATE_MODIFIED} DESC"
        private val IMAGE_PROJECTION =
            arrayOf(Media._ID, Media.DATE_MODIFIED, Media.HEIGHT, Media.WIDTH)
    }
}