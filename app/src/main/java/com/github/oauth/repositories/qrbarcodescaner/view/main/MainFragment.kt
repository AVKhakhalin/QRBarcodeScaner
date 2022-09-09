package com.github.oauth.repositories.qrbarcodescaner.view.main

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.github.oauth.repositories.qrbarcodescaner.databinding.FragmentMainBinding
import com.github.oauth.repositories.qrbarcodescaner.utils.FragmentScope
import com.github.oauth.repositories.qrbarcodescaner.utils.base.BaseFragment
import com.github.oauth.repositories.qrbarcodescaner.view.activity.MainActivity
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent

class MainFragment: BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    /** Исходные данные */ //region
    // Кнопки навигации
    private lateinit var codesScanner: Button
    private lateinit var qrCodeGenerator: Button
    // ViewModel
    private lateinit var viewModel: MainFragmentViewModel
    // MainFragmentScope
    private lateinit var showMainFragmentScope: Scope
    // newInstance для данного класса
    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }
    //endregion

    /** Работа со Scope */ //region
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Задание Scope для данного фрагмента
        showMainFragmentScope = KoinJavaComponent.getKoin().getOrCreateScope(
            FragmentScope.SHOW_MAIN_FRAGMENT_SCOPE,
            named(FragmentScope.SHOW_MAIN_FRAGMENT_SCOPE)
        )
    }
    override fun onDetach() {
        // Удаление скоупа для данного фрагмента
        showMainFragmentScope.close()
        super.onDetach()
    }
    //endregion

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Инициализация ViewModel
        initViewModel()
        // Инициализация кнопок навигации
        initButtons()
    }

    // Инициализация ViewModel
    private fun initViewModel() {
        val _viewModel: MainFragmentViewModel by showMainFragmentScope.inject()
        viewModel = _viewModel
    }

    // Инициализация кнопок навигации
    private fun initButtons() {
        codesScanner = binding.codesScannerButton
        qrCodeGenerator = binding.qrcodeGeneratorButton
        qrCodeGenerator.setOnClickListener {
            viewModel.router.navigateTo(viewModel.screens.qrCodeScreen())
            (requireActivity() as MainActivity).setIsOnlyOneFragmentExist(false)
        }
        codesScanner.setOnClickListener {
            viewModel.router.navigateTo(viewModel.screens.cameraXScreen())
            (requireActivity() as MainActivity).setIsOnlyOneFragmentExist(false)
        }
    }
}