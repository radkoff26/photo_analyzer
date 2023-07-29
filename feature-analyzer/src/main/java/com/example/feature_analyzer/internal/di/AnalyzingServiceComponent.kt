package com.example.feature_analyzer.internal.di

import android.content.Context
import com.example.base.BaseDependencies
import com.example.core_di.annotations.ServiceScope
import com.example.feature_analyzer.api.AnalyzingService
import com.example.feature_analyzer.internal.di.modules.ImageAnalyzerModule
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        ImageAnalyzerModule::class
    ],
    dependencies = [
        BaseDependencies::class
    ]
)
@ServiceScope
internal interface AnalyzingServiceComponent {

    @Component.Builder
    abstract class Builder {

        @BindsInstance
        abstract fun context(context: Context): Builder

        abstract fun baseDependencies(baseDependencies: BaseDependencies): Builder

        abstract fun build(): AnalyzingServiceComponent
    }

    fun inject(analyzingService: AnalyzingService)
}