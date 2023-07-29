package com.example.core_navigation

interface NavigationController {

    fun goToAllPhotos()

    fun goToImage(imageId: Long)

    fun goToImagesList(stringifiedType: String)

    fun back()
}
