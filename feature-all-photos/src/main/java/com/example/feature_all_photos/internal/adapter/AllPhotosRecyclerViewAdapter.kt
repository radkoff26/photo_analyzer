package com.example.feature_all_photos.internal.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.core_design.rounded_image.OnImageChangedListener
import com.example.feature_all_photos.R
import com.example.feature_all_photos.databinding.ItemAllPhotosBinding
import com.example.feature_all_photos.internal.data.ImageWithCount
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

internal class AllPhotosRecyclerViewAdapter(
    private val loadImageCallback: (imageId: Long) -> Flow<Bitmap?>,
    private val onImageClickCallback: (imageId: Long) -> Unit
) : PagingDataAdapter<ImageWithCount, AllPhotosRecyclerViewAdapter.ViewHolder>(
    ImageWithCountDiffCallback
) {
    private var adapterScope: CoroutineScope? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemAllPhotosBinding = ItemAllPhotosBinding.bind(view)
        private var currentJob: Job? = null

        init {
            setImageChangeListener()
        }

        fun bind(position: Int) {
            val imageWithObjects = getItem(position)!!

            currentJob?.cancel()

            if (!imageWithObjects.image.isProcessed) {
                binding.objectsCount.text = "-"
            } else {
                binding.objectsCount.text = imageWithObjects.count.toString()
            }

            binding.image.setOnClickListener {
                onImageClickCallback.invoke(imageWithObjects.image.id)
            }

            binding.image.setImageBitmap(null)
            currentJob = adapterScope?.launch {
                loadImageCallback.invoke(imageWithObjects.image.id).collect {
                    binding.image.setImageBitmap(it)
                }
            }
        }

        private fun setImageChangeListener() {
            binding.image.setOnImageSettledListener(object : OnImageChangedListener {

                override fun onImageSet() {
                    binding.skeleton.post {
                        binding.skeleton.showOriginal()
                    }
                }

                override fun onImageRemoved() {
                    binding.skeleton.post {
                        binding.skeleton.showSkeleton()
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
}