package com.github.oauth.repositories.qrbarcodescaner.utils

const val CICERONE_NAME: String = "cicerone"
const val MAIN_ACTIVITY_NAME: String = "MainActivity"
const val TAG_LOG: String = "mylogs"
const val PERMISSION_CAMERA_REQUEST: Int = 1
const val BASE_APPLICATION_FOLDER: String = "QR_CODES"
const val BASE_FILE_NAME: String = "QR_CODES"
const val TIME_FORMAT: String = "yyyy.MM.dd_HH:mm:ss"
const val CUTTING_PERCENTS: Float = 0.9F
const val SAVE_QRCODE_QUALITY: Int = 100

// SharedPreferences
const val SHARED_PREFERENCES_KEY: String = "Shared Preferences"
const val SHARED_PREFERENCES_CODE: String = "Shared Preferences Code"

class FragmentScope {
    companion object {
        const val SHOW_CAMERAX_FRAGMENT_SCOPE = "SHOW_CAMERAX_FRAGMENT_SCOPE"
        const val SHOW_QRCODE_FRAGMENT_SCOPE = "SHOW_QRCODE_FRAGMENT_SCOPE"
        const val SHOW_MAIN_FRAGMENT_SCOPE = "SHOW_MAIN_FRAGMENT_SCOPE"
    }
}
