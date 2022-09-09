package com.github.oauth.repositories.qrbarcodescaner.di

import com.github.oauth.repositories.qrbarcodescaner.utils.CICERONE_NAME
import com.github.oauth.repositories.qrbarcodescaner.utils.FragmentScope
import com.github.oauth.repositories.qrbarcodescaner.utils.MAIN_ACTIVITY_NAME
import com.github.oauth.repositories.qrbarcodescaner.utils.navigation.AppScreens
import com.github.oauth.repositories.qrbarcodescaner.utils.navigation.AppScreensImpl
import com.github.oauth.repositories.qrbarcodescaner.utils.resources.ResourcesProvider
import com.github.oauth.repositories.qrbarcodescaner.utils.resources.ResourcesProviderImpl
import com.github.oauth.repositories.qrbarcodescaner.view.activity.MainViewModel
import com.github.oauth.repositories.qrbarcodescaner.view.camerax.CameraXFragmentViewModel
import com.github.oauth.repositories.qrbarcodescaner.view.main.MainFragmentViewModel
import com.github.oauth.repositories.qrbarcodescaner.view.qrcode.QRCodeFragmentViewModel
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val utils = module {
    // Получение доступа к ресурсам
    single<ResourcesProvider> { ResourcesProviderImpl(androidContext()) }
}

val screens = module {
    // Scope для MainActivity
    scope(named(MAIN_ACTIVITY_NAME)) {
        viewModel {
            MainViewModel()
        }
    }
    /** Классы для навигации */  //region
    single<Cicerone<Router>>(named(CICERONE_NAME)) { Cicerone.create() }
    single<NavigatorHolder> {
        get<Cicerone<Router>>(named(CICERONE_NAME)).getNavigatorHolder()
    }
    single<Router> { get<Cicerone<Router>>(named(CICERONE_NAME)).router }
    single<AppScreens> { AppScreensImpl() }
    //endregion
    /** Классы для Scope фрагментов */ //region
    scope(named(FragmentScope.SHOW_CAMERAX_FRAGMENT_SCOPE)) {
        viewModel {
            CameraXFragmentViewModel()
        }
    }
    scope(named(FragmentScope.SHOW_QRCODE_FRAGMENT_SCOPE)) {
        viewModel {
            QRCodeFragmentViewModel()
        }
    }
    scope(named(FragmentScope.SHOW_MAIN_FRAGMENT_SCOPE)) {
        viewModel {
            MainFragmentViewModel()
        }
    }
    //endregion
}