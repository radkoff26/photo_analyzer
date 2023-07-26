package com.example.core_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core_database.dao.ImageDao
import com.example.core_database.entities.Image

@Database(
    entities = [
        Image::class
    ],
    version = 1
)
abstract class ApplicationDatabase: RoomDatabase() {

    abstract fun imageDao(): ImageDao
}