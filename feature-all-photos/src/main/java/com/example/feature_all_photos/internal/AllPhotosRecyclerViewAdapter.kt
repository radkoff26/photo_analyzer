package com.example.feature_all_photos.internal

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.core_extensions.setImage
import com.example.feature_all_photos.R

internal class AllPhotosRecyclerViewAdapter(
    private val imagesUriList: List<Uri>
) : RecyclerView.Adapter<AllPhotosRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: AppCompatImageView = view.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_all_photos, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = imagesUriList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uri = imagesUriList[position]
        holder.image.setImage(uri)
    }
}