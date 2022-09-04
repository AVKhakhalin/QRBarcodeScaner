package com.github.oauth.repositories.qrbarcodescaner.utils.base

import androidx.lifecycle.ViewModel
import com.github.oauth.repositories.qrbarcodescaner.utils.navigation.AppScreens
import com.github.terrakok.cicerone.Router
import org.koin.java.KoinJavaComponent

abstract class BaseViewModelForNavigation: ViewModel() {
    // Навигация
    val screens: AppScreens = KoinJavaComponent.getKoin().get()
    val router: Router = KoinJavaComponent.getKoin().get()
}