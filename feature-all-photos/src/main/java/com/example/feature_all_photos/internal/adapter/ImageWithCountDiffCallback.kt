package com.example.feature_all_photos.internal.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.feature_all_photos.internal.data.ImageWithCount

internal object ImageWithCountDiffCallback : DiffUtil.ItemCallback<ImageWithCount>() {

    override fun areItemsTheSame(oldItem: ImageWithCount, newItem: ImageWithCount): Boolean =
        oldItem.image.id == newItem.image.id

    override fun areContentsTheSame(oldItem: ImageWithCount, newItem: ImageWithCount): Boolean =
        oldItem.image == newItem.image && oldItem.count == newItem.count
}