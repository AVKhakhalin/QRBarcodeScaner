package com.github.oauth.repositories.qrbarcodescaner.view.camerax

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.oauth.repositories.qrbarcodescaner.R
import com.github.oauth.repositories.qrbarcodescaner.databinding.FragmentCameraxBinding
import com.github.oauth.repositories.qrbarcodescaner.utils.*
import com.github.oauth.repositories.qrbarcodescaner.utils.base.BaseFragment
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent
import java.util.concurrent.Executors

class CameraXFragment: BaseFragment<FragmentCameraxBinding>(FragmentCameraxBinding::inflate) {
    /** Задание переменных */ //region
    // ViewModel
    private lateinit var viewModel: CameraXFragmentViewModel
    // CameraXFragmentScope
    private lateinit var showCameraXFragmentScope: Scope
    // Распознанный код
    private var codeString: String = ""
    // newInstance для данного класса
    companion object {
        fun newInstance(): CameraXFragment = CameraXFragment()
    }
    // Переменные для работы с CameraX
    lateinit var previewView: PreviewView
    private var cameraProvider: ProcessCameraProvider? = null
    lateinit var cameraSelector: CameraSelector
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var previewUseCase: Preview? = null
    private var analysisUseCase: ImageAnalysis? = null
    //endregion

    /** Работа со Scope */ //region
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Задание Scope для данного фрагмента
        showCameraXFragmentScope = KoinJavaComponent.getKoin().getOrCreateScope(
            FragmentScope.SHOW_CAMERAX_FRAGMENT_SCOPE,
            named(FragmentScope.SHOW_CAMERAX_FRAGMENT_SCOPE)
        )
        // Загрузка ранее сохранённых данных в SharedPreferences
        loadSavedData()
    }
    override fun onDetach() {
        // Удаление скоупа для данного фрагмента
        showCameraXFragmentScope.close()
        super.onDetach()
    }
    //endregion

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Инициализация ViewModel
        initViewModel()
        // Установка камеры
        setupCamera()
        // Установка значения кода
        binding.scannedData.text = codeString
    }

    // Инициализация ViewModel
    private fun initViewModel() {
        val _viewModel: CameraXFragmentViewModel by showCameraXFragmentScope.inject()
        viewModel = _viewModel
    }

    private fun setupCamera() {
        previewView = binding.previewView
        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        viewModel
            .processCameraProvider
            .observe(viewLifecycleOwner) { provider: ProcessCameraProvider? ->
                cameraProvider = provider
                if (isCameraPermissionGranted()) {
                    bindCameraUseCases()
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.CAMERA),
                        PERMISSION_CAMERA_REQUEST
                    )
                }
            }
    }

    private fun bindCameraUseCases() {
        bindPreviewUseCase()
        bindAnalyseUseCase()
    }

    private fun bindPreviewUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (previewUseCase != null) {
            cameraProvider?.unbind(previewUseCase)
        }

        previewUseCase = Preview.Builder()
            .build()
        previewUseCase?.setSurfaceProvider(previewView.surfaceProvider)

        try {
            cameraProvider?.bindToLifecycle(
                this,
                cameraSelector,
                previewUseCase
            )
        } catch (illegalStateException: IllegalStateException) {
            Log.e(TAG_LOG, illegalStateException.message ?:
            "${requireContext().getString(R.string.error_title)} ${
                requireContext().getString(R.string.error_illegalstateexception)}")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.e(TAG_LOG, illegalArgumentException.message ?:
            "${requireContext().getString(R.string.error_title)} ${
                requireContext().getString(R.string.error_illegalargumentexception)}")
        }
    }

    private fun bindAnalyseUseCase() {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS).build()

        val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient(options)

        if (cameraProvider == null) {
            return
        }
        if (analysisUseCase != null) {
            cameraProvider?.unbind(analysisUseCase)
        }

        analysisUseCase = ImageAnalysis.Builder()
            .build()

        // Initialize our background executor
        val cameraExecutor = Executors.newSingleThreadExecutor()

        analysisUseCase?.setAnalyzer(
            cameraExecutor,
            ImageAnalysis.Analyzer { imageProxy ->
                processImageProxy(barcodeScanner, imageProxy)
            }
        )

        try {
            cameraProvider?.bindToLifecycle(
                this,
                cameraSelector,
                analysisUseCase
            )
        } catch (illegalStateException: IllegalStateException) {
            Log.e(TAG_LOG, illegalStateException.message ?:
            "${requireContext().getString(R.string.error_title)} ${
            requireContext().getString(R.string.error_illegalstateexception)}")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.e(TAG_LOG, illegalArgumentException.message ?:
            "${requireContext().getString(R.string.error_title)} ${
                requireContext().getString(R.string.error_illegalargumentexception)}")
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy
    ) {
        // TODO: Доработать вызов imageProxy.image
        val inputImage =
            InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)

        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                barcodes.forEach { barcode ->
                    barcode.rawValue?.let {
                        if (it.isNotEmpty()) {
                            codeString = it
                        }
                        binding.scannedData.text = codeString
                    }

                    val valueType: Int = barcode.valueType
                    // See API reference for complete list of supported types
                    when (valueType) {
                        Barcode.TYPE_WIFI -> {
                            barcode.wifi?.let {
                                val ssid = it.ssid
                                val password = it.password
                                val type = it.encryptionType
                                codeString = "${requireContext().getString(R.string.ssid)
                                } $ssid\n${requireContext().getString(R.string.password)
                                } $password\n${requireContext().getString(R.string.type)} $type"
                                binding.scannedData.text = codeString

                            }
                        }
                        Barcode.TYPE_URL -> {
                            barcode.url?.let {
                                val title = it.title
                                val url = it.url
                                codeString = "${requireContext().getString(R.string.title)
                                } $title\n${requireContext().getString(R.string.url)} $url"
                                binding.scannedData.text = codeString
                            }
                        }
                    }
                }
            }
            .addOnFailureListener {
                Log.e(TAG_LOG, it.message ?: it.toString())
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CAMERA_REQUEST) {
            if (isCameraPermissionGranted()) {
                bindCameraUseCases()
            } else {
                Log.e(TAG_LOG, "${requireContext().getString(R.string.error_title)} ${
                    requireContext().getString(R.string.error_camera_permission)}")
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Загрузка ранее сохранённых данных в SharedPreferences
    private fun loadSavedData() {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences(
                SHARED_PREFERENCES_KEY, AppCompatActivity.MODE_PRIVATE)
        codeString = sharedPreferences.getString(SHARED_PREFERENCES_CODE, "").toString()
    }

    override fun onPause() {
        super.onPause()
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
            SHARED_PREFERENCES_KEY, AppCompatActivity.MODE_PRIVATE)
        val sharedPreferencesEditor: SharedPreferences.Editor = sharedPreferences.edit()
        sharedPreferencesEditor.putString(SHARED_PREFERENCES_CODE, codeString)
        sharedPreferencesEditor.apply()
    }
}