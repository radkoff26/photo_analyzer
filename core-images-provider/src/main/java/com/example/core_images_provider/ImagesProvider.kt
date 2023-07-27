package com.example.core_images_provider

import com.example.core_database.entities.Image

interface ImagesProvider {

    suspend fun getPagedImages(currentKey: Int, pageSize: Int): List<Image>
}