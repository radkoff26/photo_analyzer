package com.example.feature_all_photos.internal.adapter.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.feature_all_photos.internal.data.ImageWithCount
import com.example.feature_all_photos.internal.data_source.ImagesMediator

internal class ImagePagingSource(
    private val imagesMediator: ImagesMediator
) : PagingSource<Int, ImageWithCount>() {

    override fun getRefreshKey(state: PagingState<Int, ImageWithCount>): Int? {
        // If the paging is empty, return null for both prevKey and nextKey
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageWithCount> {
        return try {
            val startInclusive = params.key ?: STARTING_KEY
            val pageSize = params.loadSize

            // Fetch images from the ImagesProvider using startInclusive and pageSize
            val newImages = imagesMediator.getPagedImages(startInclusive, pageSize).map {
                ImageWithCount(
                    it.image,
                    it.objects.size
                )
            }

            // Calculate the previous key and the next key
            val prevKey = if (startInclusive == STARTING_KEY) null else startInclusive - pageSize
            val nextKey = if (newImages.size < pageSize) null else startInclusive + pageSize

            LoadResult.Page(
                data = newImages,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val STARTING_KEY = 0
    }
}