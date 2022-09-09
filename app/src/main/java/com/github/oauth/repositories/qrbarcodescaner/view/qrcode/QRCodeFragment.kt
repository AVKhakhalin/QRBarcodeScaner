package com.github.oauth.repositories.qrbarcodescaner.view.qrcode

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Typeface
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import com.github.oauth.repositories.qrbarcodescaner.databinding.FragmentQrcodeBinding
import com.github.oauth.repositories.qrbarcodescaner.utils.BASE_FILE_NAME
import com.github.oauth.repositories.qrbarcodescaner.utils.FragmentScope
import com.github.oauth.repositories.qrbarcodescaner.utils.TIME_FORMAT
import com.github.oauth.repositories.qrbarcodescaner.utils.base.BaseFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class QRCodeFragment: BaseFragment<FragmentQrcodeBinding>(FragmentQrcodeBinding::inflate) {
    /** Исходные данные */ //region
    // Вывод результирующего QR-кода
    private lateinit var imageQRCode: ImageView
    // Получение исходной информации для QR-кода
    private lateinit var inputTextField: EditText
    // Запуск создания QR-кода
    private lateinit var generateQRCodeButton: Button
    // Save chooser
    private lateinit var saveChooser: SwitchCompat
    private lateinit var saveMainDepartment: TextView
    private lateinit var saveApplicationDepartment: TextView
    // ViewModel
    private lateinit var viewModel: QRCodeFragmentViewModel
    // QRCodeFragmentScope
    private lateinit var showQRCodeFragmentScope: Scope
    // newInstance для данного класса
    companion object {
        fun newInstance(): QRCodeFragment = QRCodeFragment()
    }
    //endregion

    /** Работа со Scope */ //region
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Задание Scope для данного фрагмента
        showQRCodeFragmentScope = KoinJavaComponent.getKoin().getOrCreateScope(
            FragmentScope.SHOW_QRCODE_FRAGMENT_SCOPE,
            named(FragmentScope.SHOW_QRCODE_FRAGMENT_SCOPE)
        )
    }
    override fun onDetach() {
        // Удаление скоупа для данного фрагмента
        showQRCodeFragmentScope.close()
        super.onDetach()
    }
    //endregion

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Инициализация ViewModel
        initViewModel()

        saveMainDepartment = binding.saveMainDepartmentTitle
        saveApplicationDepartment = binding.saveApplicatonDepartmentTitle
        saveChooser = binding.saveChooser.also {
            // Изменение стиля подсказок вариантов сохранения файла при работе переключателя
            it.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked) {
                    saveMainDepartment.typeface = Typeface.DEFAULT_BOLD
                    saveApplicationDepartment.typeface = Typeface.DEFAULT
                } else {
                    saveMainDepartment.typeface = Typeface.DEFAULT
                    saveApplicationDepartment.typeface = Typeface.DEFAULT_BOLD
                }
            }
        }
        imageQRCode = binding.imageQrcode
        inputTextField = binding.inputTextField
        generateQRCodeButton = binding.qrCodeGenerateButton.also {
            it.setOnClickListener {
                try {
                    val barcodeEncode: BarcodeEncoder = BarcodeEncoder()
                    val bitmap: Bitmap = barcodeEncode.encodeBitmap(
                        inputTextField.text.toString(),
                        BarcodeFormat.QR_CODE,
                        dimenDeterm(),
                        dimenDeterm()
                    )
                    imageQRCode.setImageBitmap(bitmap)
                    // Сохранение QR-кода в телефон
                    saveQRCode(bitmap)
                } catch (e: WriterException) {
                }
            }
        }
    }

    // Инициализация ViewModel
    private fun initViewModel() {
        val _viewModel: QRCodeFragmentViewModel by showQRCodeFragmentScope.inject()
        viewModel = _viewModel
    }

    // Определение максимального размера картинки QR-кода
    private fun dimenDeterm(): Int {
        val displayWidth: Int = Resources.getSystem().displayMetrics.widthPixels
        val displayHeight: Int = Resources.getSystem().displayMetrics.heightPixels
        val dimen = if (displayWidth < displayHeight) displayWidth else displayHeight
        return (dimen * 0.9).toInt()
    }

    // Сохранение QR-кода в телефоне
    private fun saveQRCode(bitmap: Bitmap) {
        try {
            // Сохранение в папке приложения
            var path = File(
                "${requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                }/QR_CODES")
            // Сохранение в основном разделе устройства
            if (!saveChooser.isChecked) path = Environment.getExternalStorageDirectory()
            // Создание директории, если она ещё не создана
            if (!path.exists()) path.mkdirs()
            Toast.makeText(requireActivity(), "$path", Toast.LENGTH_SHORT).show()

            val fileName: String = "${BASE_FILE_NAME}_${
                SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(Date())}.jpg"
            val file = File(path, fileName)
            FileOutputStream(file).use { out ->
                bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    out
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "$e", Toast.LENGTH_SHORT).show()
        }
    }
}