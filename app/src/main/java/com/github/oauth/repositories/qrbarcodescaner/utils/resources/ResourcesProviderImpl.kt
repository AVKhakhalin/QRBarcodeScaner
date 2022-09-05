package com.github.oauth.repositories.qrbarcodescaner.utils.resources

import android.content.Context

class ResourcesProviderImpl(
    val context: Context
): ResourcesProvider {
    // Получение строки из ресурсов
    override fun getString(id: Int): String {
        return context.getString(id)
    }
    // Получение массива строк из ресурсов
    override fun getStringArray(id: Int): Array<String> {
        return context.resources.getStringArray(id)
    }
}