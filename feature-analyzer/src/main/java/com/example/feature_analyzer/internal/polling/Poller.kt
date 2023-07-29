package com.example.feature_analyzer.internal.polling

internal interface Poller {

    fun startPolling(pollingCallback: PollingCallback)

    fun stopPolling()
}