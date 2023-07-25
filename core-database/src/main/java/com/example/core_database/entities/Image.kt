package com.example.core_database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

/**
 * Класс, содержащий данные о картинке из памяти устройства
 * @param id id картинки
 * @param absolutePath абсолютный путь картинки в устройстве
 * */
@Entity
data class Image(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(index = true)
    val absolutePath: String,
    val lastModificationTimestamp: Timestamp,
    val width: Int,
    val height: Int
)