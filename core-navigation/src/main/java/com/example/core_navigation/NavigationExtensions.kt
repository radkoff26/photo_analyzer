package com.example.core_navigation

import androidx.fragment.app.Fragment

fun Fragment.findNavigationController(): NavigationController =
    (requireActivity() as NavigationRoot).navigationController