package com.example.currencyxpro.ui.screens.rates

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
class RatesViewModel @Inject constructor(
    private val repository: CurrencyRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _baseCurrency = MutableStateFlow(settingsManager.getDefaultBaseCurrency())
    val baseCurrency: StateFlow<String> = _baseCurrency

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _ratesState = MutableStateFlow<Resource<Map<String, Double>>>(Resource.Loading)
    val ratesState: StateFlow<Resource<Map<String, Double>>> = _ratesState

    init {
        loadRates()
        // Listen to settings updates for default base currency
        viewModelScope.launch {
            settingsManager.defaultBaseState.collect { newDefault ->
                if (_baseCurrency.value != newDefault) {
                    _baseCurrency.value = newDefault
                    loadRates()
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

    fun setBaseCurrency(code: String) {
        if (_baseCurrency.value != code) {
            _baseCurrency.value = code
            loadRates()
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getSupportedCurrencies(): List<CurrencyInfo> {
        return repository.getSupportedCurrencies()
    }
}
