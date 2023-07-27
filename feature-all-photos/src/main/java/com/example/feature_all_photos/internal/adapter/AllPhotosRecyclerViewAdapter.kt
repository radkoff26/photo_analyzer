package com.example.feature_all_photos.internal.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.core_database.entities.ImageWithObjects
import com.example.core_design.RoundedImageView
import com.example.feature_all_photos.R
import com.faltenreich.skeletonlayout.SkeletonLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal class AllPhotosRecyclerViewAdapter(
    private val loadImageCallback: (imageId: Long) -> Flow<Bitmap?>,
    private val spanCount: Int
) : PagingDataAdapter<ImageWithObjects, AllPhotosRecyclerViewAdapter.ViewHolder>(
    ImageWithObjectsDiffCallback
) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: RoundedImageView = view.findViewById(R.id.image)
        private var skeletonLayout: SkeletonLayout = view.findViewById(R.id.skeleton)
        private var bindInvocationCount = 0

        fun bind(position: Int) {
            val imageWithObjects = getItem(position)!!
            skeletonLayout.showSkeleton()
            image.setImageBitmap(null)
            bindInvocationCount++
            val currentCount = bindInvocationCount
            CoroutineScope(Dispatchers.Main).launch {
                loadImageCallback.invoke(imageWithObjects.image.id).collect {
                    if (currentCount == bindInvocationCount) {
                        image.setImageBitmap(it)
                        // Так как bitmap в ImageView загружается не моментально,
                        // можно установить некоторую задержку выключения скелетона
                        delay(SKELETON_REMOVAL_DELAY) // Optional
                        skeletonLayout.showOriginal()
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    companion object {
        const val SKELETON_REMOVAL_DELAY = 500L
    }
}