package com.example.feature_all_photos.internal.view_model

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.core_cache.ImageCacheManager
import com.example.feature_all_photos.internal.adapter.paging.ImagePagingSource
import com.example.feature_all_photos.internal.data.ImageWithCount
import com.example.feature_all_photos.internal.data_source.ImagesMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AllPhotosFragmentViewModel @Inject constructor(
    private val imagesMediator: ImagesMediator,
    private val cacheManager: ImageCacheManager
) : ViewModel() {

    val items: Flow<PagingData<ImageWithCount>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
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

    companion object {
        private const val PAGE_SIZE = 25
    }
}