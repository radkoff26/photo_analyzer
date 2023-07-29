package com.example.feature_analyzer.internal.polling

import kotlinx.coroutines.*

internal class TimeoutPoller(private val timeoutInMillis: Long) : Poller {
    private var currentJob: Job? = null

    override fun startPolling(pollingCallback: PollingCallback) {
        currentJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                pollingCallback.poll()
                delay(timeoutInMillis)
            }
        }
    }

    override fun stopPolling() {
        currentJob?.cancel()
    }
}