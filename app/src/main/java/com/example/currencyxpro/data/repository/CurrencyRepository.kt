package com.example.currencyxpro.data.repository

import com.example.currencyxpro.data.local.entity.ConversionHistoryEntity
import com.example.currencyxpro.data.local.entity.FavoriteCurrencyEntity
import com.example.currencyxpro.data.model.CurrencyInfo
import com.example.currencyxpro.data.model.Resource
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun getSupportedCurrencies(): List<CurrencyInfo>

    suspend fun getLatestRates(
        baseCurrency: String,
        forceRefresh: Boolean = false
    ): Resource<Map<String, Double>>

    suspend fun getHistoricalRates(
        baseCurrency: String,
        targetCurrency: String,
        days: Int
    ): Resource<List<Pair<String, Double>>>

    // Favorites
    fun getFavoritesFlow(): Flow<List<FavoriteCurrencyEntity>>
    suspend fun addFavorite(code: String)
    suspend fun removeFavorite(code: String)
    fun isFavoriteFlow(code: String): Flow<Boolean>
    suspend fun isFavorite(code: String): Boolean

    // Favorite Pairs
    fun getFavoritePairsFlow(): Flow<List<com.example.currencyxpro.data.local.entity.FavoritePairEntity>>
    suspend fun addFavoritePair(base: String, target: String)
    suspend fun removeFavoritePair(base: String, target: String)
    fun isFavoritePairFlow(base: String, target: String): Flow<Boolean>
    suspend fun isFavoritePair(base: String, target: String): Boolean

    // History
    fun getHistoryFlow(): Flow<List<ConversionHistoryEntity>>
    suspend fun insertHistory(
        fromCurrency: String,
        toCurrency: String,
        fromAmount: Double,
        toAmount: Double,
        rate: Double
    )
    suspend fun deleteHistory(history: ConversionHistoryEntity)
    suspend fun clearHistory()
}
