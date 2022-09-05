package com.github.oauth.repositories.qrbarcodescaner.view.activity

import com.github.oauth.repositories.qrbarcodescaner.utils.base.BaseViewModelForNavigation
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

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