package com.example.core_images_provider

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore.Images.Media
import android.util.Log
import kotlin.math.min

class DeviceImagesProvider(private val context: Context) : ImagesProvider {
    private val contentUri = Media.INTERNAL_CONTENT_URI
    private val cursor by lazy {
        context.contentResolver.query(
            contentUri,
            arrayOf(Media._ID, Media.DATA),
            null,
            null,
            null
        )!!
    }

    private val lock = Any()

    @Volatile
    private var currentPosition = 0

    override fun getNextNPhotos(n: Int): List<Uri>? {
        if (currentPosition >= cursor.count) {
            return null
        }

        val imagesList: MutableList<Uri> = ArrayList()

        val imageIdColumnIndex = cursor.getColumnIndex(Media._ID)

        synchronized(lock) {
            val bound = min(cursor.count, currentPosition + n)
            while (currentPosition < bound) {
                cursor.moveToPosition(currentPosition)
                val imageId = cursor.getLong(imageIdColumnIndex)
                Log.d("QWERTYUIOP", "getNextNPhotos: $imageId")
                val imageContentUri = ContentUris.withAppendedId(contentUri, imageId)
                imagesList.add(imageContentUri)
                currentPosition++
            }
        }

        return imagesList
    }
}