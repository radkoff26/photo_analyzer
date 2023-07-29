package com.example.core_navigation

interface NavigationController {

    fun goToAllImages()

    fun goToImage(imageId: Long)

    fun goToImagesList(stringifiedType: String)

    fun back()
}
