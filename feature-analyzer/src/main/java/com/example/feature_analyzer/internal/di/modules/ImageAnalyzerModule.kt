package com.example.feature_analyzer.internal.di.modules

import android.content.Context
import com.example.core_ai.ObjectDetectorHelper
import com.example.core_analyzer.ImageAnalyzer
import com.example.core_analyzer.ImageAnalyzerImpl
import com.example.core_di.annotations.ServiceScope
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
internal interface ImageAnalyzerModule {

    companion object {
        @Provides
        @ServiceScope
        fun provideObjectDetectorHelper(context: Context): ObjectDetectorHelper =
            ObjectDetectorHelper(context = context)

        @Provides
        @ServiceScope
        fun provideImageAnalyzerImpl(objectDetectorHelper: ObjectDetectorHelper): ImageAnalyzerImpl =
            ImageAnalyzerImpl(objectDetectorHelper)
    }

    @Binds
    @ServiceScope
    fun bindImageAnalyzer(impl: ImageAnalyzerImpl): ImageAnalyzer
}