package com.example.feature_images_list.internal.adapter.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core_database.ApplicationDatabase
import com.example.core_database.entities.ImageWithObjects
import com.example.core_database.entities.ObjectOnImageType

internal class ImageWithTypePagingSource(
    private val applicationDatabase: ApplicationDatabase,
    private val currentType: ObjectOnImageType
) : PagingSource<Int, ImageWithObjects>() {

    override fun getRefreshKey(state: PagingState<Int, ImageWithObjects>): Int? {
        // If the paging is empty, return null for both prevKey and nextKey
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageWithObjects> {
        return try {
            val startInclusive = params.key ?: STARTING_KEY
            val pageSize = params.loadSize

            // Fetch images from the ImagesProvider using startInclusive and pageSize
            val newImages =
                applicationDatabase.imageWithObjectsDao().getPagedImagesContainingObjectsWithType(
                    startInclusive,
                    pageSize,
                    currentType
                )

            Log.d("QWERTYUIOP", "load: $newImages")

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