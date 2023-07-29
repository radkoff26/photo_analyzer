package com.example.photoanalyzer.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.NavHostFragment
import com.example.core_navigation.NavigationController
import com.example.core_navigation.NavigationRoot
import com.example.photoanalyzer.R
import com.example.photoanalyzer.navigation.NavigationControllerImpl

class MainActivity : AppCompatActivity(), NavigationRoot {

    override lateinit var navigationController: NavigationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findViewById<FragmentContainerView>(R.id.nav_host_fragment)
            .getFragment<NavHostFragment>()
            .navController

        navigationController = NavigationControllerImpl(navController)
    }
}
