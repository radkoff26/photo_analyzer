package com.example.photoanalyzer.app

import android.app.Application
import android.content.Intent
import com.example.base.BaseDependencies
import com.example.base.BaseDependenciesProvider
import com.example.base.DaggerBaseComponent
import com.example.feature_analyzer.api.AnalyzingService

class App : Application(), BaseDependenciesProvider {

    override val baseDependencies: BaseDependencies by lazy {
        DaggerBaseComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()

        // Запускаем polling-сервис анализирования картинок устройства
        startService(Intent(this, AnalyzingService::class.java))
    }
}
