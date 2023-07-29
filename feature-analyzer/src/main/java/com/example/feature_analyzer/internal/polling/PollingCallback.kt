package com.example.feature_analyzer.internal.polling

internal fun interface PollingCallback {

    suspend fun poll()
}