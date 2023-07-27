package com.example.core_database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.core_database.entities.Image

@Dao
interface ImageDao {

    @Query("SELECT * FROM Image")
    suspend fun getAllImages(): List<Image>

    @Query("SELECT * FROM Image ORDER BY lastModificationTimestamp LIMIT :loadSize OFFSET :start")
    suspend fun getPagedImages(start: Int, loadSize: Int): List<Image>
}