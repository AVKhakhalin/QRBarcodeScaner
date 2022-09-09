package com.github.oauth.repositories.qrbarcodescaner.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.oauth.repositories.qrbarcodescaner.R
import com.github.oauth.repositories.qrbarcodescaner.databinding.ActivityMainBinding
import com.github.oauth.repositories.qrbarcodescaner.utils.MAIN_ACTIVITY_NAME
import com.github.oauth.repositories.qrbarcodescaner.utils.navigation.BackButtonListener
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
    private var isOnlyOneFragmentExist: Boolean = false
    // ViewModel
    private val mainActivityScope: Scope = KoinJavaComponent.getKoin().getOrCreateScope(
        MAIN_ACTIVITY_NAME, named(MAIN_ACTIVITY_NAME)
    )
    private lateinit var viewModel: MainViewModel
    // Binding
    private lateinit var binding: ActivityMainBinding
    //endregion

    /** Методы для настройки навигатора */ //region
    override fun onResume() {
        super.onResume()
        // Установка навигатора
        navigatorHolder.setNavigator(navigator)
    }
    override fun onPause() {
        super.onPause()
        // Удаление навигатора
        navigatorHolder.removeNavigator()
    }
    override fun onBackPressed() {
        // Закрыть активити, если в настоящий момент открыт только один фрагмент
        if (isOnlyOneFragmentExist) finish()
        isOnlyOneFragmentExist = supportFragmentManager.fragments.size == 1
        supportFragmentManager.fragments.forEach {
            if (it is BackButtonListener && it.backPressed()) {
                return
            }
        }
        viewModel.router.exit()
    }
    fun setIsOnlyOneFragmentExist(isOnlyOneFragmentExist: Boolean) {
        this.isOnlyOneFragmentExist = isOnlyOneFragmentExist
    }
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Подключение Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Создание Scope для MainActivity
        val viewModel: MainViewModel by mainActivityScope.inject()
        this.viewModel = viewModel
        // Отслеживание первого или последующего запусков MainActivity
        if (savedInstanceState != null) {
            // Установка текущего экрана приложения
            navigatorHolder.setNavigator(navigator)
        } else {
            // Начальная загрузка основного экрана при старте приложения
            viewModel.router.navigateTo(viewModel.screens.mainScreen())
        }
        // Отображение содержимого окна
        setContentView(binding.root)
    }
}