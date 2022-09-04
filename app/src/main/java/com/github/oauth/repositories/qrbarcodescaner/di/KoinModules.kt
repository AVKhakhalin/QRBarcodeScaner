package com.github.oauth.repositories.qrbarcodescaner.di

import com.github.oauth.repositories.qrbarcodescaner.utils.CICERONE_NAME
import com.github.oauth.repositories.qrbarcodescaner.utils.MAIN_ACTIVITY_NAME
import com.github.oauth.repositories.qrbarcodescaner.utils.navigation.AppScreens
import com.github.oauth.repositories.qrbarcodescaner.utils.navigation.AppScreensImpl
import com.github.oauth.repositories.qrbarcodescaner.view.activity.MainViewModel
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

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
}