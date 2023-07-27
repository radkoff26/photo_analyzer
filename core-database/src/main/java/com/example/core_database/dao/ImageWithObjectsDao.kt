package com.example.core_database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.core_database.entities.ImageWithObjects

@Dao
interface ImageWithObjectsDao {

    @Query("SELECT * FROM Image")
    suspend fun getAllImages(): List<ImageWithObjects>

    @Query("SELECT * FROM Image LIMIT :pageSize OFFSET :startPosition")
    suspend fun getPagedImages(startPosition: Int, pageSize: Int): List<ImageWithObjects>

    @Query("SELECT COUNT(*) == 0 FROM Image")
    suspend fun isDatabaseEmpty(): Boolean
}
