package com.example.feature_all_photos.internal.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core_di.annotations.FragmentScope
import com.example.core_view_model.ViewModelFactory
import com.example.core_view_model.ViewModelKey
import com.example.core_view_model.ViewModelMap
import com.example.feature_all_photos.internal.view_model.AllPhotosFragmentViewModel
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
    @ViewModelKey(AllPhotosFragmentViewModel::class)
    fun bindAllPhotosViewModel(impl: AllPhotosFragmentViewModel): ViewModel
}