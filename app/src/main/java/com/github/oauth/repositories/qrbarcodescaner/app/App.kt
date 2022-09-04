package com.github.oauth.repositories.qrbarcodescaner.app

import android.app.Application
import com.github.oauth.repositories.qrbarcodescaner.di.screens
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        // Koin
        startKoin {
            androidContext(applicationContext)
            modules(listOf(screens))
        }
    }

    companion object {
        var instance: App? = null
    }
}