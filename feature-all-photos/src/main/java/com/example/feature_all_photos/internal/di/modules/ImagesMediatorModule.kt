package com.example.feature_all_photos.internal.di.modules

import com.example.core_database.ApplicationDatabase
import com.example.core_di.annotations.FragmentScope
import com.example.core_images_provider.ImagesProvider
import com.example.feature_all_photos.internal.data_source.ImagesMediator
import dagger.Module
import dagger.Provides

@Module
internal object ImagesMediatorModule {

    @Provides
    @FragmentScope
    fun provideImagesMediator(
        applicationDatabase: ApplicationDatabase,
        imagesProvider: ImagesProvider
    ): ImagesMediator = ImagesMediator(imagesProvider, applicationDatabase)
}