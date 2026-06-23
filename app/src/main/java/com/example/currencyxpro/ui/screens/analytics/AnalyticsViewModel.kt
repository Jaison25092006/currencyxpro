package com.example.currencyxpro.ui.screens.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyxpro.data.local.SettingsManager
import com.example.currencyxpro.data.model.CurrencyData
import com.example.currencyxpro.data.model.CurrencyInfo
import com.example.currencyxpro.data.model.Resource
import com.example.currencyxpro.data.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val repository: CurrencyRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _baseCurrency = MutableStateFlow(settingsManager.getDefaultBaseCurrency())
    val baseCurrency: StateFlow<String> = _baseCurrency

    private val _targetCurrency = MutableStateFlow(
        if (settingsManager.getDefaultBaseCurrency() == "USD") "EUR" else "USD"
    )
    val targetCurrency: StateFlow<String> = _targetCurrency

    // Time ranges: 7 days, 30 days, 90 days
    private val _timeRangeDays = MutableStateFlow(30)
    val timeRangeDays: StateFlow<Int> = _timeRangeDays

    private val _historyDataState = MutableStateFlow<Resource<List<Pair<String, Double>>>>(Resource.Loading)
    val historyDataState: StateFlow<Resource<List<Pair<String, Double>>>> = _historyDataState

    init {
        loadHistoricalRates()
        viewModelScope.launch {
            settingsManager.defaultBaseState.collect { newDefault ->
                if (_baseCurrency.value != newDefault) {
                    _baseCurrency.value = newDefault
                    _targetCurrency.value = if (newDefault == "USD") "EUR" else "USD"
                    loadHistoricalRates()
                }
            }
        }
    }

    fun loadHistoricalRates() {
        viewModelScope.launch {
            _historyDataState.value = Resource.Loading
            val resource = repository.getHistoricalRates(
                baseCurrency = _baseCurrency.value,
                targetCurrency = _targetCurrency.value,
                days = _timeRangeDays.value
            )
            _historyDataState.value = resource
        }
    }

    fun setBaseCurrency(code: String) {
        if (_baseCurrency.value != code) {
            _baseCurrency.value = code
            if (_targetCurrency.value == code) {
                _targetCurrency.value = if (code == "USD") "EUR" else "USD"
            }
            loadHistoricalRates()
        }
    }

    fun setTargetCurrency(code: String) {
        if (_targetCurrency.value != code) {
            _targetCurrency.value = code
            loadHistoricalRates()
        }
    }

    fun setTimeRangeDays(days: Int) {
        if (_timeRangeDays.value != days) {
            _timeRangeDays.value = days
            loadHistoricalRates()
        }
    }

    fun swapCurrencies() {
        val oldBase = _baseCurrency.value
        val oldTarget = _targetCurrency.value
        _baseCurrency.value = oldTarget
        _targetCurrency.value = oldBase
        loadHistoricalRates()
    }

    fun getSupportedCurrencies(): List<CurrencyInfo> {
        return repository.getSupportedCurrencies()
    }
}
