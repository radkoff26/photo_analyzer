package com.example.core_extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import java.io.FileInputStream

fun Context.getImageBitmapByUri(uri: Uri): Bitmap? {
    try {
        contentResolver.openInputStream(uri).use {
            return BitmapFactory.decodeStream(it)
        }
    } catch (e: Exception) {
        Log.e("QWERTYUIOP", "getImageBitmapByUri: error", e)
        return null
    }
}

//fun Context.getImageBitmapByUri(uri: Uri): Bitmap? {
//    try {
//        FileInputStream(uri.toFile()).use {
//            return BitmapFactory.decodeStream(it)
//        }
//    } catch (e: Exception) {
//        Log.e("QWERTYUIOP", "getImageBitmapByUri: error", e)
//        return null
//    }
//}