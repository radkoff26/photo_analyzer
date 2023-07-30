package com.example.feature_images_list.internal.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core_di.annotations.FragmentScope
import com.example.core_view_model.ViewModelFactory
import com.example.core_view_model.ViewModelKey
import com.example.core_view_model.ViewModelMap
import com.example.feature_images_list.internal.view_model.ImagesListFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    companion object {
        @Provides
        @FragmentScope
        fun provideViewModelFactor(map: ViewModelMap): ViewModelProvider.Factory =
            ViewModelFactory(map)
    }

    @Binds
    @FragmentScope
    @IntoMap
    @ViewModelKey(ImagesListFragmentViewModel::class)
    fun bindImagesListViewModel(impl: ImagesListFragmentViewModel): ViewModel
}