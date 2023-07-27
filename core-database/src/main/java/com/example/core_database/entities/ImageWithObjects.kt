package com.example.core_database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ImageWithObjects(
    @Embedded
    val image: Image,
    @Relation(parentColumn = "id", entityColumn = "imageId")
    val objects: List<ObjectOnImage>
)
