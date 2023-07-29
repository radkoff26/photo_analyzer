package com.example.feature_all_photos.internal.data_source

import com.example.core_database.ApplicationDatabase
import com.example.core_database.entities.ImageWithObjects
import com.example.core_images_provider.ImagesProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ImagesMediator constructor(
    private val imagesProvider: ImagesProvider,
    private val applicationDatabase: ApplicationDatabase
) {

    suspend fun getPagedImages(startPosition: Int, pageSize: Int): List<ImageWithObjects> =
        withContext(Dispatchers.IO) {
            val imageWithObjectsDao = applicationDatabase.imageWithObjectsDao()
            val imagesWithObjects = getPagedImagesFromImagesProvider(startPosition, pageSize)
            return@withContext imagesWithObjects.map {
                val imageWithObjects = imageWithObjectsDao.getImageById(it.image.id)
                if (
                    imageWithObjects != null
                    && imageWithObjects.image.isProcessed
                    && imageWithObjects.image.lastModificationTimestamp == it.image.lastModificationTimestamp
                ) {
                    imageWithObjects
                } else {
                    it
                }
            }
        }

    private suspend fun getPagedImagesFromImagesProvider(
        startPosition: Int,
        pageSize: Int
    ): List<ImageWithObjects> {
        val retrievedImages = imagesProvider.getPagedImages(startPosition, pageSize)
        return retrievedImages.map {
            ImageWithObjects(
                it,
                emptyList()
            )
        }
    }
}