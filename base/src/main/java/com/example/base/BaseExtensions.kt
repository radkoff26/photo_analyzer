package com.example.base

import android.app.Application

fun Application.getBaseDependencies(): BaseDependencies =
    (this as BaseDependenciesProvider).baseDependencies
