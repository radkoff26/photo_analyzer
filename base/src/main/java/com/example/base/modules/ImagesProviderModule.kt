package com.example.base.modules

import android.content.Context
import com.example.core_images_provider.DeviceImagesProvider
import com.example.core_images_provider.ImagesProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal interface ImagesProviderModule {

    companion object {
        @Provides
        @Singleton
        fun provideDeviceImagesProvider(context: Context): DeviceImagesProvider =
            DeviceImagesProvider(context)
    }

    @Binds
    @Singleton
    fun bindImagesProvider(impl: DeviceImagesProvider): ImagesProvider
}