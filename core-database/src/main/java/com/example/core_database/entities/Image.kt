package com.example.core_database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

/**
 * Класс, содержащий данные о картинке из внешней памяти устройства.
 * @param id id картинки относительно External Storage ContentProvider
 * @param lastModificationTimestamp момент времени последнего изменения картинки
 * @param height высота картинки
 * @param width ширина картинки
 * */
@Entity(tableName = "image")
data class Image(
    @PrimaryKey
    val id: Long,
    val lastModificationTimestamp: Timestamp,
    val height: Int,
    val width: Int,
    val isProcessed: Boolean = false
)