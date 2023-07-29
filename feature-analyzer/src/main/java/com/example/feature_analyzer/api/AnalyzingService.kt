package com.example.feature_analyzer.api

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import com.example.base.getBaseDependencies
import com.example.core_actions.Actions
import com.example.core_analyzer.ImageAnalyzer
import com.example.core_database.ApplicationDatabase
import com.example.core_database.dao.ImageWithObjectsDao
import com.example.core_database.entities.Image
import com.example.core_extensions.getImageFromExternalStorageByIdSyncScaledDown
import com.example.core_images_provider.ImagesProvider
import com.example.feature_analyzer.internal.di.DaggerAnalyzingServiceComponent
import com.example.feature_analyzer.internal.polling.Poller
import com.example.feature_analyzer.internal.polling.PollingCallback
import com.example.feature_analyzer.internal.polling.TimeoutPoller
import javax.inject.Inject

class AnalyzingService : Service() {
    @Inject
    internal lateinit var imageAnalyzer: ImageAnalyzer

    @Inject
    internal lateinit var imagesProvider: ImagesProvider

    @Inject
    internal lateinit var applicationDatabase: ApplicationDatabase

    private val pollingCallback: PollingCallback = PollingCallback {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return@PollingCallback
        }

        val allDeviceExternalImages = imagesProvider.getAllImagesSync()
        val imageWithObjectsDao = applicationDatabase.imageWithObjectsDao()

        allDeviceExternalImages.forEach {
            val imageFromDatabase = imageWithObjectsDao.getImageById(it.id)

            if (
                imageFromDatabase == null
                || !imageFromDatabase.image.isProcessed
                || it.lastModificationTimestamp != imageFromDatabase.image.lastModificationTimestamp
            ) {
                analyzeImageAndInsertIntoDatabase(it, imageWithObjectsDao)
                sendBroadcast(Intent(Actions.DATABASE_UPDATED_ACTION))
            }
        }
    }

    private val poller: Poller = TimeoutPoller(TIMEOUT)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        DaggerAnalyzingServiceComponent.builder()
            .baseDependencies(application.getBaseDependencies())
            .context(this)
            .build()
            .inject(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        poller.startPolling(pollingCallback)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        poller.stopPolling()
        super.onDestroy()
    }

    private suspend fun analyzeImageAndInsertIntoDatabase(image: Image, dao: ImageWithObjectsDao) {
        val imageBitmap = getImageFromExternalStorageByIdSyncScaledDown(image.id)
        if (imageBitmap != null) {
            val analyzedImage = imageAnalyzer.analyzeImage(image, imageBitmap)
            dao.insertImageWithObjects(analyzedImage)
        }
    }

    companion object {
        private const val TIMEOUT = 30_000L // 30s
        const val TAG = "AnalyzingServiceClass"
    }
}