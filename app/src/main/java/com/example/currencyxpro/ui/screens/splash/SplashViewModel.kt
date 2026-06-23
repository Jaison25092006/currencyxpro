package com.example.currencyxpro.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyxpro.data.local.SettingsManager
import com.example.currencyxpro.data.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: CurrencyRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady

    init {
        initializeApp()
    }

    private fun initializeApp() {
        viewModelScope.launch {
            // Background pre-fetch of the default base currency rates
            val defaultBase = settingsManager.getDefaultBaseCurrency()
            repository.getLatestRates(defaultBase, forceRefresh = true)
            
            // Elegant delay for the transition
            delay(1800)
            _isReady.value = true
        }
    }
}
