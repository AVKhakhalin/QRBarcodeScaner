package com.github.oauth.repositories.qrbarcodescaner.view.camerax

import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.oauth.repositories.qrbarcodescaner.R
import com.github.oauth.repositories.qrbarcodescaner.utils.TAG_LOG
import com.github.oauth.repositories.qrbarcodescaner.utils.base.BaseViewModelForNavigation
import com.github.oauth.repositories.qrbarcodescaner.utils.resources.ResourcesProviderImpl
import org.koin.java.KoinJavaComponent
import java.util.concurrent.ExecutionException

class CameraXFragmentViewModel: BaseViewModelForNavigation() {
    /** Исходные даннные */ //region
    // LiveData
    private var cameraProviderLiveData: MutableLiveData<ProcessCameraProvider>? = null
    // Получение доступа к ресурсам
    private val resourcesProviderImpl: ResourcesProviderImpl = KoinJavaComponent.getKoin().get()
    //endregion

    val processCameraProvider: LiveData<ProcessCameraProvider>
        get() {
            if (cameraProviderLiveData == null) {
                cameraProviderLiveData = MutableLiveData()
                val cameraProviderFuture =
                    ProcessCameraProvider.getInstance(resourcesProviderImpl.context)
                cameraProviderFuture.addListener(
                    Runnable {
                        try {
                            cameraProviderLiveData!!.setValue(cameraProviderFuture.get())
                        } catch (e: ExecutionException) {
                            Log.e(TAG_LOG,
                                "${resourcesProviderImpl.getString(R.string.error_title)} ", e)
                        } catch (e: InterruptedException) {
                            Log.e(TAG_LOG,
                                "${resourcesProviderImpl.getString(R.string.error_title)} ", e)
                        }
                    },
                    ContextCompat.getMainExecutor(resourcesProviderImpl.context)
                )
            }
            return cameraProviderLiveData!!
        }
}