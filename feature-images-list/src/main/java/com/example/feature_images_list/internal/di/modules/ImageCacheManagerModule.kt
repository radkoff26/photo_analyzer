package com.example.feature_images_list.internal.di.modules

import android.content.Context
import com.example.core_cache.ImageCacheManager
import com.example.core_cache.ImageCacheManagerImpl
import com.example.core_di.annotations.FragmentScope
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface ImageCacheManagerModule {

    companion object {
        @Provides
        @FragmentScope
        fun provideImageCacheManagerImpl(context: Context): ImageCacheManagerImpl =
            ImageCacheManagerImpl(context)
    }

    @Binds
    @FragmentScope
    fun bindImageCacheManager(impl: ImageCacheManagerImpl): ImageCacheManager
}