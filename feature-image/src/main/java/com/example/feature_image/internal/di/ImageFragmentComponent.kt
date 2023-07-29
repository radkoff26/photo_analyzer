package com.example.feature_image.internal.di

import android.content.Context
import com.example.base.BaseDependencies
import com.example.core_di.annotations.FragmentScope
import com.example.feature_image.api.ImageFragment
import com.example.feature_image.internal.di.modules.ImageAnalyzerModule
import com.example.feature_image.internal.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        ImageAnalyzerModule::class,
        ViewModelModule::class
    ],
    dependencies = [
        BaseDependencies::class
    ]
)
@FragmentScope
internal interface ImageFragmentComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
            baseDependencies: BaseDependencies
        ): ImageFragmentComponent
    }

    fun inject(imageFragment: ImageFragment)
}