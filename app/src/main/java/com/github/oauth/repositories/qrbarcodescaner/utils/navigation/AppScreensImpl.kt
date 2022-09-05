package com.github.oauth.repositories.qrbarcodescaner.utils.navigation

import com.github.oauth.repositories.qrbarcodescaner.view.camerax.CameraXFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

class AppScreensImpl: AppScreens {
    //region About
    override fun cameraXScreen() = FragmentScreen {
        CameraXFragment.newInstance()
    }
    //endregion
}