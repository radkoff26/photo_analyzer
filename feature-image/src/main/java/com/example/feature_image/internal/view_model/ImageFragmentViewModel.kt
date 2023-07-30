package com.example.feature_image.internal.view_model

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_analyzer.ImageAnalyzer
import com.example.core_database.ApplicationDatabase
import com.example.core_database.entities.Image
import com.example.core_database.entities.ImageWithObjects
import com.example.core_database.entities.ObjectOnImage
import com.example.core_database.entities.ObjectOnImageType
import com.example.core_images_provider.ImagesProvider
import com.example.feature_image.internal.loader.ImageLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ImageFragmentViewModel @Inject constructor(
    private val applicationDatabase: ApplicationDatabase,
    private val imageAnalyzer: ImageAnalyzer,
    private val imagesProvider: ImagesProvider,
    private val imageLoader: ImageLoader
) : ViewModel() {
    private val _imageBitmapLiveData: MutableLiveData<Bitmap?> = MutableLiveData(null)
    val imageBitmapLiveData: LiveData<Bitmap?> = _imageBitmapLiveData

    private var imageId: Long? = null
    private var type: ObjectOnImageType? = null
    private var imageWithObjects: ImageWithObjects? = null

    @JvmOverloads
    fun init(imageId: Long, type: ObjectOnImageType? = null) {
        this.imageId = imageId
        this.type = type
    }

    fun requestImageLoading() {
        imageId!! // Needs to be initialized
        if (_imageBitmapLiveData.value == null) {
            viewModelScope.launch(Dispatchers.IO) {
                val bitmap = imageLoader.loadImage(imageId!!)
                _imageBitmapLiveData.postValue(bitmap)
            }
        }
    }

    fun getLoadedImage(): Image? = imageWithObjects?.image

    suspend fun getObjectsKeySortedAndFilteredIfNecessary(): Map<String, ObjectOnImage> {
        imageId!! // Needs to be initialized
        return if (type == null) {
            getObjectsKeySorted()
        } else {
            getObjectsKeySorted().filter {
                it.value.type == type
            }
        }
    }

    private suspend fun getObjectsKeySorted(): Map<String, ObjectOnImage> {
        if (imageWithObjects != null) {
            return extractObjectsAndFormMap(imageWithObjects!!).toSortedMap()
        }
        imageWithObjects = getObjectsOfImageByImageIdAndUpdateDatabaseIfNecessary(imageId!!)
        return extractObjectsAndFormMap(imageWithObjects!!).toSortedMap()
    }

    private suspend fun getObjectsOfImageByImageIdAndUpdateDatabaseIfNecessary(imageId: Long): ImageWithObjects =
        withContext(Dispatchers.IO) {
            val imageWithObjects = applicationDatabase.imageWithObjectsDao().getImageById(imageId)
            if (imageWithObjects == null || !imageWithObjects.image.isProcessed) {
                // Presume that image still exists in device external storage
                val bitmap = imageLoader.loadImage(imageId)!!
                val imageInstance = imagesProvider.getImageById(imageId)!!
                val analyzedImage = imageAnalyzer.analyzeImage(imageInstance, bitmap)
                applicationDatabase.imageWithObjectsDao().insertImageWithObjects(analyzedImage)
                if (type == null) {
                    analyzedImage
                } else {
                    analyzedImage.copy(
                        image = analyzedImage.image,
                        objects = analyzedImage.objects.filter {
                            it.type == type
                        }
                    )
                }
            } else {
                imageWithObjects
            }
        }

    /**
     * Данная функция строит map для объектов, где каждый объект имеет свой уникальный ключ.
     * Это нужно для ситуаций, когда несколько одинаковых объектов присутствуют на картинке.
     * */
    private suspend fun extractObjectsAndFormMap(imageWithObjects: ImageWithObjects): Map<String, ObjectOnImage> =
        withContext(Dispatchers.IO) {
            val counterMap = HashMap<ObjectOnImageType, Int>()
            val resultMap = HashMap<String, ObjectOnImage>()
            val objectsList = imageWithObjects.objects

            objectsList.forEach {
                val type = it.type
                counterMap.putIfAbsent(type, 0)
                val currentElementCount = counterMap[type]!! + 1
                // Данный уникальный ключ будет отображаться как имя объекта в списке
                // Это нужно для более удобного различения объектов
                val uniqueKey = "$type $currentElementCount"
                counterMap[type] = currentElementCount
                resultMap[uniqueKey] = it
            }

            resultMap
        }
}