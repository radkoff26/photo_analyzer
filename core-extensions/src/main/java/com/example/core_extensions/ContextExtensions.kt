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
const val MAX_DIMENSION = 400

/**
 * Функция, возвращающая Flow с определённой уменьшенной картинкой из External Storage.
 * @param imageId id картинки, которую нужно получить
 * @return Flow, который эммитит bitmap картинки
 * */
fun Context.getImageFromExternalStorageByIdScaledDown(
    imageId: Long,
    maxDimension: Int = MAX_DIMENSION
): Flow<Bitmap?> {
    return flow {
        val bitmap = getImageFromExternalStorageByIdSyncScaledDown(imageId, maxDimension)
        emit(bitmap)
    }.flowOn(Dispatchers.IO)
}

/**
 * Функция, возвращающая уменьшенный bitmap определённой картинки из External Storage синхронно.
 * @param imageId id картинки, которую нужно получить
 * @return bitmap картинки или null, если произошла ошибка
 * */
fun Context.getImageFromExternalStorageByIdSyncScaledDown(
    imageId: Long,
    maxDimension: Int = MAX_DIMENSION
): Bitmap? {
    try {
        val uri =
            ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId)
        contentResolver.openInputStream(uri).use {
            val bitmap = BitmapFactory.decodeStream(it)
            return scaleBitmapToMaxDimension(bitmap, maxDimension)
        }
    } catch (e: Exception) {
        return null
    }
}

/**
 * Функция для уменьшения bitmap'а до минимального размера стороны с сохранением соотношения сторон.
 * @param bitmap bitmap картинки, которую нужно уменьшить
 * @return уменьшенный bitmap
 * */
private fun scaleBitmapToMaxDimension(bitmap: Bitmap, maxDimension: Int): Bitmap {
    val maxBitmapDimension = max(bitmap.height, bitmap.width)
    val scaledHeight: Int
    val scaledWidth: Int
    val aspectRatio = bitmap.width.toFloat() / bitmap.height
    if (maxBitmapDimension > maxDimension) {
        if (bitmap.height > bitmap.width) {
            scaledHeight = maxDimension
            scaledWidth = (scaledHeight * aspectRatio).roundToInt()
        } else {
            scaledWidth = maxDimension
            scaledHeight = (scaledWidth / aspectRatio).roundToInt()
        }
    } else {
        scaledWidth = bitmap.width
        scaledHeight = bitmap.height
    }
    return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, false)
}