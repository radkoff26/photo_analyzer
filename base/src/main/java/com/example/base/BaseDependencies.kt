package com.example.base

import com.example.core_database.ApplicationDatabase
import com.example.core_images_provider.ImagesProvider

interface BaseDependencies {
    val applicationDatabase: ApplicationDatabase
    val imagesProvider: ImagesProvider
}