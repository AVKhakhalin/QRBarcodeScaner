package com.github.oauth.repositories.qrbarcodescaner.view.activity

import com.github.oauth.repositories.qrbarcodescaner.utils.base.BaseViewModelForNavigation
import kotlinx.coroutines.*

class MainViewModel: BaseViewModelForNavigation() {
    /** Задание переменных */ //region
    val mainViewModelInteractor: MainViewModelInteractor = MainViewModelInteractor()
    // ViewModelCoroutineScope
    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        })
    //endregion

    private fun handleError(error: Throwable) {
    }
}