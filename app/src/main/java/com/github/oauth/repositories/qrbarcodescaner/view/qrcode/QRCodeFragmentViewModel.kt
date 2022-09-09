package com.github.oauth.repositories.qrbarcodescaner.view.qrcode

import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.github.oauth.repositories.qrbarcodescaner.R
import com.github.oauth.repositories.qrbarcodescaner.model.AppState
import com.github.oauth.repositories.qrbarcodescaner.utils.*
import com.github.oauth.repositories.qrbarcodescaner.utils.base.BaseViewModel
import com.github.oauth.repositories.qrbarcodescaner.utils.resources.ResourcesProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.koin.java.KoinJavaComponent
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class QRCodeFragmentViewModel: BaseViewModel<AppState>() {
    /** Исходные данные */ //region
    private val resourcesProviderImpl: ResourcesProvider = KoinJavaComponent.getKoin().get()
    // Информация с результатом запроса
    private val liveDataForViewToObserve: LiveData<AppState> = _mutableLiveData
    //endregion
    fun createAndSaveQRCode(text: String, saveType: Boolean) {
        try {
            val barcodeEncode: BarcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncode.encodeBitmap(
                text,
                BarcodeFormat.QR_CODE,
                dimenDeterm(),
                dimenDeterm()
            )
            _mutableLiveData.value = AppState.Success(bitmap)
            // Сохранение QR-кода в телефон
            saveQRCode(bitmap, saveType)
        } catch (e: WriterException) {
            Toast.makeText(resourcesProviderImpl.getContext(), "$e", Toast.LENGTH_SHORT).show()
        }
    }

    // Определение максимального размера картинки QR-кода
    private fun dimenDeterm(): Int {
        val displayWidth: Int = Resources.getSystem().displayMetrics.widthPixels
        val displayHeight: Int = Resources.getSystem().displayMetrics.heightPixels
        val dimen = if (displayWidth < displayHeight) displayWidth else displayHeight
        return (dimen * CUTTING_PERCENTS).toInt()
    }

    // Сохранение QR-кода в телефоне
    private fun saveQRCode(bitmap: Bitmap, saveType: Boolean) {
        try {
            // Сохранение в папке приложения
            var path = File(
                "${resourcesProviderImpl.getContext().getExternalFilesDir(
                Environment.DIRECTORY_PICTURES)}/$BASE_APPLICATION_FOLDER")
            // Сохранение в основном разделе устройства
            if (saveType) path = Environment.getExternalStorageDirectory()
            // Создание директории, если она ещё не создана
            if (!path.exists()) path.mkdirs()
            Toast.makeText(resourcesProviderImpl.getContext(),
                "${resourcesProviderImpl.getString(R.string.save_qrcode_note)}\n$path",
                Toast.LENGTH_LONG).show()

            val fileName: String = "${BASE_FILE_NAME}_${
                SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(Date())}.jpg"
            val file = File(path, fileName)
            FileOutputStream(file).use { out ->
                bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    SAVE_QRCODE_QUALITY,
                    out
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(resourcesProviderImpl.getContext(), "$e", Toast.LENGTH_SHORT).show()
        }
    }

    fun subscribe(): LiveData<AppState> {
        return liveDataForViewToObserve
    }

    override fun handleError(error: Throwable) {
        _mutableLiveData.postValue(AppState.Error(error))
    }
}