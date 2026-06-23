package com.example.currencyxpro.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyxpro.data.local.entity.ConversionHistoryEntity
import com.example.currencyxpro.data.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: CurrencyRepository
) : ViewModel() {

    val historyState: StateFlow<List<ConversionHistoryEntity>> = repository.getHistoryFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteHistory(history: ConversionHistoryEntity) {
        viewModelScope.launch {
            repository.deleteHistory(history)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}
