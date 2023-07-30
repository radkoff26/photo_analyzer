package com.example.feature_all_photos.internal.data

import com.example.core_database.entities.Image

data class ImageWithCount(
    val image: Image,
    var count: Int
)
