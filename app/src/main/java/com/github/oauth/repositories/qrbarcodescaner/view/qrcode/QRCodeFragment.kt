package com.github.oauth.repositories.qrbarcodescaner.view.qrcode

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import com.github.oauth.repositories.qrbarcodescaner.databinding.FragmentQrcodeBinding
import com.github.oauth.repositories.qrbarcodescaner.model.AppState
import com.github.oauth.repositories.qrbarcodescaner.utils.FragmentScope
import com.github.oauth.repositories.qrbarcodescaner.utils.base.BaseFragment
import com.google.zxing.WriterException
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent

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
                viewModel.createAndSaveQRCode(
                    inputTextField.text.toString(), !saveChooser.isChecked)
            }
        }
    }

    // Инициализация ViewModel
    private fun initViewModel() {
        val _viewModel: QRCodeFragmentViewModel by showQRCodeFragmentScope.inject()
        viewModel = _viewModel
        // Подписка на ViewModel
        this.viewModel.subscribe().observe(viewLifecycleOwner) { renderData(it) }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                // Изменение внешнего вида индикатора загрузки
                binding.progressbar.visibility = View.INVISIBLE
                try {
                    imageQRCode.setImageBitmap(appState.bitmap)
                } catch (e: WriterException) {
                    Toast.makeText(requireActivity(), "$e", Toast.LENGTH_SHORT).show()
                }
            }
            is AppState.Loading -> {
                // Изменение внешнего вида индикатора загрузки
                binding.progressbar.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                // Изменение внешнего вида индикатора загрузки
                binding.progressbar.visibility = View.INVISIBLE
                // Уведомление пользователя об ошибке
                Toast.makeText(requireActivity(), "${appState.error.message}",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}