package com.example.core_database.entities

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Класс, представляющий собой объединённый объединение картинки и объектов, которые ей принадлежат.
 * @param image инстанс картинки
 * @param objects список объектов, принадлежащий картинке
 * */
data class ImageWithObjects(
    @Embedded
    val image: Image,
    @Relation(parentColumn = "id", entityColumn = "imageId")
    val objects: List<ObjectOnImage>
)
