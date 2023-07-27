package com.example.core_extensions

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Integer.max
import kotlin.math.roundToInt

/**
 * Максимальный размер стороны картинки.
 * */
const val MAX_DIMENSION = 640

/**
 * Функция, возвращающая Flow с определённой картинкой из External Storage.
 * @param imageId id картинки, которую нужно получить
 * @return Flow, который эммитит bitmap картинки
 * */
fun Context.getImageFromExternalStorageById(imageId: Long): Flow<Bitmap?> {
    return flow {
        try {
            val uri =
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId)
            contentResolver.openInputStream(uri).use {
                val bitmap = BitmapFactory.decodeStream(it)
                val scaledBitmap = scaleBitmapToMaxDimension(bitmap)
                emit(scaledBitmap)
            }
        } catch (e: Exception) {
            emit(null)
        }
    }.flowOn(Dispatchers.IO)
}

/**
 * Функция для уменьшения bitmap'а до минимального размера стороны с сохранением соотношения сторон.
 * @param bitmap bitmap картинки, которую нужно уменьшить
 * @return уменьшенный bitmap
 * */
private fun scaleBitmapToMaxDimension(bitmap: Bitmap): Bitmap {
    val maxBitmapDimension = max(bitmap.height, bitmap.width)
    val scaledHeight: Int
    val scaledWidth: Int
    val aspectRatio = bitmap.width.toFloat() / bitmap.height
    if (maxBitmapDimension > MAX_DIMENSION) {
        if (bitmap.height > bitmap.width) {
            scaledHeight = MAX_DIMENSION
            scaledWidth = (scaledHeight * aspectRatio).roundToInt()
        } else {
            scaledWidth = MAX_DIMENSION
            scaledHeight = (scaledWidth / aspectRatio).roundToInt()
        }
    } else {
        scaledWidth = bitmap.width
        scaledHeight = bitmap.height
    }
    return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, false)
}