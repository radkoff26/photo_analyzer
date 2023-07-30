package com.example.feature_images_list.internal.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.core_database.entities.ImageWithObjects

internal object ImageWithObjectsDiffCallback : DiffUtil.ItemCallback<ImageWithObjects>() {

    override fun areItemsTheSame(oldItem: ImageWithObjects, newItem: ImageWithObjects): Boolean =
        oldItem.image.id == newItem.image.id

    override fun areContentsTheSame(oldItem: ImageWithObjects, newItem: ImageWithObjects): Boolean =
        oldItem.image == newItem.image && oldItem.objects.size == newItem.objects.size
}