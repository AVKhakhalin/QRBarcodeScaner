package com.github.oauth.repositories.qrbarcodescaner.utils.navigation

import com.github.oauth.repositories.qrbarcodescaner.view.camerax.CameraXFragment
import com.github.oauth.repositories.qrbarcodescaner.view.main.MainFragment
import com.github.oauth.repositories.qrbarcodescaner.view.qrcode.QRCodeFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

class AppScreensImpl: AppScreens {
    //region Считывание QR-, штрих-кода
    override fun cameraXScreen() = FragmentScreen {
        CameraXFragment.newInstance()
    }
    //endregion

    //region создание QR-кода
    override fun qrCodeScreen() = FragmentScreen {
        QRCodeFragment.newInstance()
    }
    //endregion

    //region создание главного окна с кнопками
    override fun mainScreen() = FragmentScreen {
        MainFragment.newInstance()
    }
    //endregion
}