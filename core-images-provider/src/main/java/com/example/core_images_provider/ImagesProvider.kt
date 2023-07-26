package com.example.core_images_provider

import kotlinx.coroutines.flow.Flow

interface ImagesProvider {

    fun getNextNPhotos(n: Int): Flow<List<Long>?>
}