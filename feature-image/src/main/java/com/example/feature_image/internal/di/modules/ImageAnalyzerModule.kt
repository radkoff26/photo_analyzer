package com.example.feature_image.internal.di.modules

import android.content.Context
import com.example.core_ai.ObjectDetectorHelper
import com.example.core_analyzer.ImageAnalyzer
import com.example.core_analyzer.ImageAnalyzerImpl
import com.example.core_di.annotations.FragmentScope
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
internal interface ImageAnalyzerModule {

    companion object {
        @Provides
        @FragmentScope
        fun provideObjectDetectorHelper(context: Context): ObjectDetectorHelper =
            ObjectDetectorHelper(context = context)

        @Provides
        @FragmentScope
        fun provideImageAnalyzerImpl(objectDetectorHelper: ObjectDetectorHelper): ImageAnalyzerImpl =
            ImageAnalyzerImpl(objectDetectorHelper)
    }

    @Binds
    @FragmentScope
    fun bindImageAnalyzer(impl: ImageAnalyzerImpl): ImageAnalyzer
}