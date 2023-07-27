package com.example.feature_all_photos.internal.di

import android.content.Context
import com.example.base.BaseDependencies
import com.example.core_di.annotations.FragmentScope
import com.example.feature_all_photos.api.AllPhotosFragment
import com.example.feature_all_photos.internal.di.modules.ImageCacheManagerModule
import com.example.feature_all_photos.internal.di.modules.ImagesMediatorModule
import com.example.feature_all_photos.internal.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        ImageCacheManagerModule::class,
        ImagesMediatorModule::class,
        ViewModelModule::class
    ],
    dependencies = [
        BaseDependencies::class
    ]
)
@FragmentScope
internal interface AllPhotosFragmentComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
            baseDependencies: BaseDependencies
        ): AllPhotosFragmentComponent
    }

    fun inject(fragment: AllPhotosFragment)
}