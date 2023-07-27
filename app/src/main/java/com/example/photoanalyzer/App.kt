package com.example.photoanalyzer

import android.app.Application
import com.example.base.BaseDependencies
import com.example.base.BaseDependenciesProvider
import com.example.base.DaggerBaseComponent

class App : Application(), BaseDependenciesProvider {

    override val baseDependencies: BaseDependencies by lazy {
        DaggerBaseComponent.factory().create(this)
    }
}
