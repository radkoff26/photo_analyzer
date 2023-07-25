package com.example.core_extensions

import android.net.Uri
import androidx.appcompat.widget.AppCompatImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun AppCompatImageView.setImage(uri: Uri) {
    CoroutineScope(Dispatchers.Default).launch {
        val bitmap = context.getImageBitmapByUri(uri)
        CoroutineScope(Dispatchers.Main).launch {
            setImageBitmap(bitmap)
        }
    }
}