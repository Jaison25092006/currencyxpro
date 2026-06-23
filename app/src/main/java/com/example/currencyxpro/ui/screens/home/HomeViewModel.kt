package com.example.currencyxpro.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyxpro.data.local.SettingsManager
import com.example.currencyxpro.data.model.CurrencyData
import com.example.currencyxpro.data.model.CurrencyInfo
import com.example.currencyxpro.data.model.Resource
import com.example.currencyxpro.data.repository.CurrencyRepository
import com.example.currencyxpro.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CurrencyRepository,
    private val settingsManager: SettingsManager,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _baseCurrency = MutableStateFlow(settingsManager.getDefaultBaseCurrency())
    val baseCurrency: StateFlow<String> = _baseCurrency

    private val _targetCurrency = MutableStateFlow(
        if (settingsManager.getDefaultBaseCurrency() == "USD") "EUR" else "USD"
    )
    val targetCurrency: StateFlow<String> = _targetCurrency

    private val _amount = MutableStateFlow("1")
    val amount: StateFlow<String> = _amount

    private val _ratesState = MutableStateFlow<Resource<Map<String, Double>>>(Resource.Loading)
    val ratesState: StateFlow<Resource<Map<String, Double>>> = _ratesState

    val isBaseFavorite: StateFlow<Boolean> = repository.isFavoriteFlow(_baseCurrency.value)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        loadRates()
        // Listen to preference changes
        viewModelScope.launch {
            settingsManager.defaultBaseState.collect { newDefault ->
                if (_baseCurrency.value != newDefault) {
                    _baseCurrency.value = newDefault
                    _targetCurrency.value = if (newDefault == "USD") "EUR" else "USD"
                    loadRates()
                }
            }
        }
        // Auto-sync when internet connection returns
        viewModelScope.launch {
            var wasOffline = false
            networkMonitor.isOnline.collect { online ->
                if (online) {
                    if (wasOffline || _ratesState.value is Resource.Error) {
                        loadRates(forceRefresh = true)
                    }
                    wasOffline = false
                } else {
                    wasOffline = true
                }
            }
        }
    }

    fun loadRates(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _ratesState.value = Resource.Loading
            val resource = repository.getLatestRates(_baseCurrency.value, forceRefresh)
            _ratesState.value = resource
        }
    }

    fun updateAmount(newAmount: String) {
        // Enforce valid decimal numbers
        if (newAmount.isEmpty() || newAmount.matches(Regex("^\\d*\\.?\\d*$"))) {
            _amount.value = newAmount
        }
    }

    fun setBaseCurrency(code: String) {
        if (_baseCurrency.value != code) {
            _baseCurrency.value = code
            if (_targetCurrency.value == code) {
                _targetCurrency.value = if (code == "USD") "EUR" else "USD"
            }
            loadRates()
        }
    }

    fun setTargetCurrency(code: String) {
        if (_targetCurrency.value != code) {
            _targetCurrency.value = code
        }
    }

    fun swapCurrencies() {
        val oldBase = _baseCurrency.value
        val oldTarget = _targetCurrency.value
        _baseCurrency.value = oldTarget
        _targetCurrency.value = oldBase
        loadRates()
    }

    fun toggleFavoriteBase() {
        viewModelScope.launch {
            val code = _baseCurrency.value
            if (repository.isFavorite(code)) {
                repository.removeFavorite(code)
            } else {
                repository.addFavorite(code)
            }
        }
    }

    fun saveConversionToHistory(fromAmt: Double, toAmt: Double, rate: Double) {
        viewModelScope.launch {
            repository.insertHistory(
                fromCurrency = _baseCurrency.value,
                toCurrency = _targetCurrency.value,
                fromAmount = fromAmt,
                toAmount = toAmt,
                rate = rate
            )
        }
    }

    fun getSupportedCurrencies(): List<CurrencyInfo> {
        return repository.getSupportedCurrencies()
    }
}
