package com.example.feature_all_photos.internal.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.feature_all_photos.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

internal class AllPhotosRecyclerViewAdapter(
    private val imagesUriList: List<Long>,
    private val loadImageCallback: (imageId: Long) -> Flow<Bitmap?>
) : RecyclerView.Adapter<AllPhotosRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: AppCompatImageView = view.findViewById(R.id.image)
        private var bindInvocationCount = 0

        fun bind(position: Int) {
            val imageId = imagesUriList[position]
            image.setImageBitmap(null)
            bindInvocationCount++
            val currentCount = bindInvocationCount
            CoroutineScope(Dispatchers.Main).launch {
                loadImageCallback.invoke(imageId).collect {
                    if (currentCount == bindInvocationCount) {
                        image.setImageBitmap(it)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_all_photos, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = imagesUriList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }
}