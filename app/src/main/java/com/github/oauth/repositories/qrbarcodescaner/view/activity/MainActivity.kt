package com.github.oauth.repositories.qrbarcodescaner.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.oauth.repositories.qrbarcodescaner.R
import com.github.oauth.repositories.qrbarcodescaner.databinding.ActivityMainBinding
import com.github.oauth.repositories.qrbarcodescaner.utils.MAIN_ACTIVITY_NAME
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent

class MainActivity: AppCompatActivity() {
    /** Задание переменных */ //region
    // Навигация
    private val navigator =
        AppNavigator(this@MainActivity, R.id.activity_fragments_container)
    private val navigatorHolder: NavigatorHolder = KoinJavaComponent.getKoin().get()
    // ViewModel
    private val mainActivityScope: Scope = KoinJavaComponent.getKoin().getOrCreateScope(
        MAIN_ACTIVITY_NAME, named(MAIN_ACTIVITY_NAME)
    )
    private lateinit var viewModel: MainViewModel
    // Binding
    private lateinit var binding: ActivityMainBinding
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Подключение Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Создание Scope для MainActivity
        val viewModel: MainViewModel by mainActivityScope.inject()
        this.viewModel = viewModel
        // Отображение содержимого окна
        setContentView(binding.root)
    }
}