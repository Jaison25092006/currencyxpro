package com.example.currencyxpro.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyxpro.data.local.SettingsManager
import com.example.currencyxpro.data.local.entity.FavoriteCurrencyEntity
import com.example.currencyxpro.data.local.entity.FavoritePairEntity
import com.example.currencyxpro.data.model.CurrencyData
import com.example.currencyxpro.data.model.CurrencyInfo
import com.example.currencyxpro.data.model.Resource
import com.example.currencyxpro.data.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: CurrencyRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _baseCurrency = MutableStateFlow(settingsManager.getDefaultBaseCurrency())
    val baseCurrency: StateFlow<String> = _baseCurrency

    val favoritesState: StateFlow<List<FavoriteCurrencyEntity>> = repository.getFavoritesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoritePairsState: StateFlow<List<FavoritePairEntity>> = repository.getFavoritePairsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _ratesState = MutableStateFlow<Resource<Map<String, Double>>>(Resource.Loading)
    val ratesState: StateFlow<Resource<Map<String, Double>>> = _ratesState

    init {
        loadRates()
        viewModelScope.launch {
            settingsManager.defaultBaseState.collect { newDefault ->
                if (_baseCurrency.value != newDefault) {
                    _baseCurrency.value = newDefault
                    loadRates()
                }
            }
        }
    }

    fun loadRates() {
        viewModelScope.launch {
            _ratesState.value = Resource.Loading
            val resource = repository.getLatestRates(_baseCurrency.value, forceRefresh = false)
            _ratesState.value = resource
        }
    }

    fun setBaseCurrency(code: String) {
        if (_baseCurrency.value != code) {
            _baseCurrency.value = code
            loadRates()
        }
    }

    fun removeFavorite(code: String) {
        viewModelScope.launch {
            repository.removeFavorite(code)
        }
    }

    fun addFavorite(code: String) {
        viewModelScope.launch {
            repository.addFavorite(code)
        }
    }

    fun removeFavoritePair(base: String, target: String) {
        viewModelScope.launch {
            repository.removeFavoritePair(base, target)
        }
    }

    fun addFavoritePair(base: String, target: String) {
        viewModelScope.launch {
            repository.addFavoritePair(base, target)
        }
    }

    fun getSupportedCurrencies(): List<CurrencyInfo> {
        return repository.getSupportedCurrencies()
    }
}
