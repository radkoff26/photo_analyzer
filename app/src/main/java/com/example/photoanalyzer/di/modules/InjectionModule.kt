package com.example.photoanalyzer.di.modules

import com.example.core_di.annotations.ActivityScope
import com.example.core_di.annotations.FragmentScope
import com.example.feature_all_photos.api.AllPhotosFragment
import com.example.photoanalyzer.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface InjectionModule {

    @ContributesAndroidInjector
    @ActivityScope
    fun activityMain(): MainActivity

    @ContributesAndroidInjector
    @FragmentScope
    fun fragmentAllPhotos(): AllPhotosFragment
}