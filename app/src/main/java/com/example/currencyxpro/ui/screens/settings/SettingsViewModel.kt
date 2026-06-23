package com.example.currencyxpro.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyxpro.data.local.SettingsManager
import com.example.currencyxpro.data.local.dao.ExchangeRateDao
import com.example.currencyxpro.data.model.CurrencyData
import com.example.currencyxpro.data.model.CurrencyInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsManager: SettingsManager,
    private val exchangeRateDao: ExchangeRateDao
) : ViewModel() {

    val themeState: StateFlow<Int> = settingsManager.themeState
    val defaultBaseCurrency: StateFlow<String> = settingsManager.defaultBaseState
    val notificationsState: StateFlow<Boolean> = settingsManager.notificationsState

    fun setTheme(theme: Int) {
        settingsManager.setTheme(theme)
    }

    fun setDefaultBaseCurrency(code: String) {
        settingsManager.setDefaultBaseCurrency(code)
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        settingsManager.setNotificationsEnabled(enabled)
    }

    fun clearCache() {
        viewModelScope.launch {
            exchangeRateDao.clearCachedRates()
        }
    }

    fun getSupportedCurrencies(): List<CurrencyInfo> {
        return CurrencyData.supportedCurrencies
    }
}
