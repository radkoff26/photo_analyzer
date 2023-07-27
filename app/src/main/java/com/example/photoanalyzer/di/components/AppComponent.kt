package com.example.photoanalyzer.di.components

import android.content.Context
import com.example.photoanalyzer.App
import com.example.photoanalyzer.di.modules.DatabaseModule
import com.example.photoanalyzer.di.modules.ImagesProviderModule
import com.example.photoanalyzer.di.modules.InjectionModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        DatabaseModule::class,
        ImagesProviderModule::class,
        InjectionModule::class
    ]
)
@Singleton
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}