package com.example.feature_all_photos.internal.view_model

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.core_cache.ImageCacheManagerImpl
import com.example.core_database.entities.ImageWithObjects
import com.example.feature_all_photos.internal.adapter.paging.ImagePagingSource
import com.example.feature_all_photos.internal.data_source.ImagesMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AllPhotosFragmentViewModel @Inject constructor(
    context: Context,
    private val imagesMediator: ImagesMediator
) : ViewModel() {
    private val cacheManager = ImageCacheManagerImpl(context)

    val items: Flow<PagingData<ImageWithObjects>> =
        Pager(
            config = PagingConfig(
                PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ImagePagingSource(imagesMediator)
            }
        ).flow.cachedIn(viewModelScope)

    override fun onCleared() {
        super.onCleared()
        cacheManager.onClear()
    }

    fun loadImageByImageId(imageId: Long): Flow<Bitmap?> = cacheManager.loadImage(imageId)

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val context: Context,
        private val imagesMediator: ImagesMediator
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AllPhotosFragmentViewModel(context, imagesMediator) as T
        }
    }

    companion object {
        private const val PAGE_SIZE = 30
    }
}