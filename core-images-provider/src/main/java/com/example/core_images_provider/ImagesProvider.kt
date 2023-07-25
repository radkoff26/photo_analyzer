package com.example.core_images_provider

import android.net.Uri

interface ImagesProvider {

    fun getNextNPhotos(n: Int): List<Uri>?
}