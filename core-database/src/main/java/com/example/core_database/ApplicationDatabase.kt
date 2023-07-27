package com.example.core_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.core_database.converters.TimestampConverter
import com.example.core_database.dao.ImageDao
import com.example.core_database.dao.ImageWithObjectsDao
import com.example.core_database.entities.Image
import com.example.core_database.entities.ObjectOnImage

@Database(
    entities = [
        Image::class,
        ObjectOnImage::class
    ],
    version = 1
)
@TypeConverters(
    value = [
        TimestampConverter::class
    ]
)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun imageDao(): ImageDao

    abstract fun imageWithObjectsDao(): ImageWithObjectsDao

    companion object {
        const val DATABASE_NAME = "app_database"
    }
}