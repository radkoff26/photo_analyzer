package com.example.photoanalyzer.navigation

import android.os.Bundle
import androidx.navigation.NavController
import com.example.core_navigation.NavigationController
import com.example.feature_image.api.ImageFragment
import com.example.photoanalyzer.R

class NavigationControllerImpl(private val navController: NavController) : NavigationController {

    override fun goToAllImages() {
        navController.navigate(R.id.all_photos_fragment)
    }

    override fun goToImage(imageId: Long) {
        val bundle = Bundle().apply {
            putLong(ImageFragment.Contraction.IMAGE_ID, imageId)
        }
        navController.navigate(R.id.image_fragment, bundle)
    }

    override fun goToImagesList(stringifiedType: String) {
        TODO("Not yet implemented")
    }

    override fun back() {
        navController.popBackStack()
    }
}