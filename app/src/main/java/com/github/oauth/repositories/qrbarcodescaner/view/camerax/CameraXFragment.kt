package com.github.oauth.repositories.qrbarcodescaner.view.camerax

import android.content.Context
import android.os.Bundle
import android.view.View
import com.github.oauth.repositories.qrbarcodescaner.databinding.FragmentCameraxBinding
import com.github.oauth.repositories.qrbarcodescaner.utils.FragmentScope
import com.github.oauth.repositories.qrbarcodescaner.utils.base.BaseFragment
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent

class CameraXFragment: BaseFragment<FragmentCameraxBinding>(FragmentCameraxBinding::inflate) {
    /** Задание переменных */ //region
    // ViewModel
    private lateinit var viewModel: CameraXFragmentViewModel
    // CameraXFragmentScope
    private lateinit var showCameraXFragmentScope: Scope
    // newInstance для данного класса
    companion object {
        fun newInstance(): CameraXFragment = CameraXFragment()
    }
    //endregion

    /** Работа со Scope */ //region
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Задание Scope для данного фрагмента
        showCameraXFragmentScope = KoinJavaComponent.getKoin().getOrCreateScope(
            FragmentScope.SHOW_CAMERAX_FRAGMENT_SCOPE,
            named(FragmentScope.SHOW_CAMERAX_FRAGMENT_SCOPE)
        )
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
    }

    // Инициализация ViewModel
    private fun initViewModel() {
        val _viewModel: CameraXFragmentViewModel by showCameraXFragmentScope.inject()
        viewModel = _viewModel
    }
}