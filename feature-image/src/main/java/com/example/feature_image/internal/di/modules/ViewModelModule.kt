package com.example.feature_image.internal.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core_di.annotations.FragmentScope
import com.example.core_view_model.ViewModelFactory
import com.example.core_view_model.ViewModelKey
import com.example.core_view_model.ViewModelMap
import com.example.feature_image.internal.view_model.ImageFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
internal interface ViewModelModule {

    companion object {
        @Provides
        @FragmentScope
        fun provideViewModelFactory(map: ViewModelMap): ViewModelProvider.Factory =
            ViewModelFactory(map)
    }

    @Binds
    @FragmentScope
    @IntoMap
    @ViewModelKey(ImageFragmentViewModel::class)
    fun bindImageViewModel(impl: ImageFragmentViewModel): ViewModel
}