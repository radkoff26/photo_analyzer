package com.example.base

import android.content.Context
import com.example.base.modules.DatabaseModule
import com.example.base.modules.ImagesProviderModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        DatabaseModule::class,
        ImagesProviderModule::class
    ]
)
@Singleton
interface BaseComponent : BaseDependencies {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): BaseComponent
    }
}