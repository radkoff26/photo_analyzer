package com.example.feature_all_photos.internal.view_model

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core_cache.ImageCacheManager
import kotlinx.coroutines.flow.Flow

class AllPhotosFragmentViewModel(context: Context): ViewModel() {
    private val cacheManager = ImageCacheManager(context)

    override fun onCleared() {
        super.onCleared()
        cacheManager.onClear()
    }

    fun loadImageByImageId(imageId: Long): Flow<Bitmap?> = cacheManager.loadImage(imageId)

    @Suppress("UNCHECKED_CAST")
    class Factory(private val context: Context): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AllPhotosFragmentViewModel(context) as T
        }
    }
}