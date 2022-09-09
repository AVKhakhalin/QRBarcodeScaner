package com.github.oauth.repositories.qrbarcodescaner.model

import android.graphics.Bitmap

sealed class AppState {
    data class Success(val bitmap: Bitmap?): AppState()
    data class Error(val error: Throwable): AppState()
    data class Loading(val progress: Int?): AppState()
}