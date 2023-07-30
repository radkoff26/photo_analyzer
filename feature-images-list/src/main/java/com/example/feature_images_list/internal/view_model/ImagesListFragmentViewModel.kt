package com.example.feature_images_list.internal.view_model

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.core_cache.ImageCacheManager
import com.example.core_database.ApplicationDatabase
import com.example.core_database.entities.ImageWithObjects
import com.example.core_database.entities.ObjectOnImageType
import com.example.feature_images_list.internal.adapter.paging.ImageWithTypePagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImagesListFragmentViewModel @Inject constructor(
    val type: ObjectOnImageType,
    private val applicationDatabase: ApplicationDatabase,
    private val imageCacheManager: ImageCacheManager
) : ViewModel() {
    val items: Flow<PagingData<ImageWithObjects>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ImageWithTypePagingSource(applicationDatabase, type)
            }
        ).flow

    fun loadImage(imageId: Long): Flow<Bitmap?> = imageCacheManager.loadImage(imageId)

    override fun onCleared() {
        super.onCleared()
        imageCacheManager.onClear()
    }

    companion object {
        private const val PAGE_SIZE = 25
    }
}