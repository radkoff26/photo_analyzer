package com.example.core_database.dao

import androidx.room.*
import com.example.core_database.entities.Image
import com.example.core_database.entities.ImageWithObjects
import com.example.core_database.entities.ObjectOnImage

@Dao
interface ImageWithObjectsDao {

    @Query("SELECT * FROM Image")
    suspend fun getAllImages(): List<ImageWithObjects>

    @Query("SELECT * FROM Image LIMIT :pageSize OFFSET :startPosition")
    suspend fun getPagedImages(startPosition: Int, pageSize: Int): List<ImageWithObjects>

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
