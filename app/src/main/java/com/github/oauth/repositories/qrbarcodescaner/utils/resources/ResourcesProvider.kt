package com.github.oauth.repositories.qrbarcodescaner.utils.resources

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes

interface ResourcesProvider {
    fun getString(@StringRes id: Int): String
    fun getStringArray(@ArrayRes id: Int): Array<String>
}