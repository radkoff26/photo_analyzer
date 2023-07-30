package com.example.core_database.dao

import androidx.room.*
import com.example.core_database.entities.Image
import com.example.core_database.entities.ImageWithObjects
import com.example.core_database.entities.ObjectOnImage
import com.example.core_database.entities.ObjectOnImageType

@Dao
interface ImageWithObjectsDao {

    @Query("SELECT * FROM image")
    suspend fun getAllImages(): List<ImageWithObjects>

    @Query("SELECT * FROM image LIMIT :pageSize OFFSET :startPosition")
    suspend fun getPagedImages(startPosition: Int, pageSize: Int): List<ImageWithObjects>

    @Query(
        "SELECT DISTINCT imageId FROM object " +
                "INNER JOIN image ON object.imageId = image.id " +
                "WHERE object.type = :type " +
                "ORDER BY lastModificationTimestamp DESC " +
                "LIMIT :pageSize OFFSET :startPosition"
    )
    suspend fun getPagedImagesIdsWithObjectOfType(
        startPosition: Int,
        pageSize: Int,
        type: ObjectOnImageType
    ): List<Long>

    suspend fun getPagedImagesContainingObjectsWithType(
        startPosition: Int,
        pageSize: Int,
        type: ObjectOnImageType
    ): List<ImageWithObjects> {
        val imagesIds = getPagedImagesIdsWithObjectOfType(startPosition, pageSize, type)
        return imagesIds.map { imageId ->
            val imageWithObjects = getImageById(imageId)!!
            imageWithObjects.copy(
                image = imageWithObjects.image,
                objects = imageWithObjects.objects.filter {
                    it.type == type
                }
            )
        }
    }

    @Query("SELECT * FROM Image WHERE id = :id")
    suspend fun getImageById(id: Long): ImageWithObjects?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(vararg images: Image)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObjects(vararg objects: ObjectOnImage)

    @Query("SELECT COUNT(*) == 0 FROM Image")
    suspend fun isDatabaseEmpty(): Boolean

    @Transaction
    suspend fun insertImageWithObjects(imageWithObjects: ImageWithObjects) {
        insertImages(imageWithObjects.image)
        insertObjects(*imageWithObjects.objects.toTypedArray())
    }
}
