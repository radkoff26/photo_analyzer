package com.example.feature_images_list.internal.di

import android.content.Context
import com.example.base.BaseDependencies
import com.example.core_database.entities.ObjectOnImageType
import com.example.core_di.annotations.FragmentScope
import com.example.feature_images_list.api.ImagesListFragment
import com.example.feature_images_list.internal.di.modules.ImageCacheManagerModule
import com.example.feature_images_list.internal.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        ImageCacheManagerModule::class,
        ViewModelModule::class
    ],
    dependencies = [
        BaseDependencies::class
    ]
)
@FragmentScope
interface ImagesListFragmentComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
            @BindsInstance type: ObjectOnImageType,
            baseDependencies: BaseDependencies
        ): ImagesListFragmentComponent
    }

    fun inject(imagesListFragment: ImagesListFragment)
}