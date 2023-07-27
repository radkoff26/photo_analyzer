package com.example.core_database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ObjectOnImage(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val imageId: Long,
    val type: ObjectOnMapType,
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
)