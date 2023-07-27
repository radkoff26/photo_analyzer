package com.example.feature_all_photos.internal.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.core_database.entities.ImageWithObjects
import com.example.core_design.OnImageChangedListener
import com.example.core_design.RoundedImageView
import com.example.feature_all_photos.R
import com.faltenreich.skeletonlayout.SkeletonLayout
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

internal class AllPhotosRecyclerViewAdapter(
    private val loadImageCallback: (imageId: Long) -> Flow<Bitmap?>,
    private val spanCount: Int
) : PagingDataAdapter<ImageWithObjects, AllPhotosRecyclerViewAdapter.ViewHolder>(
    ImageWithObjectsDiffCallback
) {
    private var adapterScope: CoroutineScope? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: RoundedImageView = view.findViewById(R.id.image)
        private var skeletonLayout: SkeletonLayout = view.findViewById(R.id.skeleton)
        private var currentJob: Job? = null

        init {
            setImageChangeListener()
        }

        fun bind(position: Int) {
            val imageWithObjects = getItem(position)!!

            currentJob?.cancel()

            image.setImageBitmap(null)
            currentJob = adapterScope?.launch {
                loadImageCallback.invoke(imageWithObjects.image.id).collect {
                    image.setImageBitmap(it)
                }
            }
        }

        private fun setImageChangeListener() {
            image.setOnImageSettledListener(object : OnImageChangedListener {

                override fun onImageSet() {
                    skeletonLayout.post {
                        skeletonLayout.showOriginal()
                    }
                }

                override fun onImageRemoved() {
                    skeletonLayout.post {
                        skeletonLayout.showSkeleton()
                    }
                }
            })
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

    fun refreshScope() {
        adapterScope = CoroutineScope(Dispatchers.Main + Job())
    }

    fun cancelScope() {
        adapterScope?.cancel()
        adapterScope = null
    }

    companion object {
        const val SKELETON_REMOVAL_DELAY = 500L
    }
}