package com.github.oauth.repositories.qrbarcodescaner.utils.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen

interface AppScreens {
    fun cameraXScreen(): FragmentScreen
    fun qrCodeScreen(): FragmentScreen
    fun mainScreen(): FragmentScreen
}