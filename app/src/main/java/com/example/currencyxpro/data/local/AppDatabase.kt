package com.example.currencyxpro.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currencyxpro.data.local.dao.ExchangeRateDao
import com.example.currencyxpro.data.local.dao.FavoriteDao
import com.example.currencyxpro.data.local.dao.FavoritePairDao
import com.example.currencyxpro.data.local.dao.HistoryDao
import com.example.currencyxpro.data.local.entity.ConversionHistoryEntity
import com.example.currencyxpro.data.local.entity.ExchangeRateEntity
import com.example.currencyxpro.data.local.entity.FavoriteCurrencyEntity
import com.example.currencyxpro.data.local.entity.FavoritePairEntity

@Database(
    entities = [
        FavoriteCurrencyEntity::class,
        ConversionHistoryEntity::class,
        ExchangeRateEntity::class,
        FavoritePairEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun favoritePairDao(): FavoritePairDao
    abstract fun historyDao(): HistoryDao
    abstract fun exchangeRateDao(): ExchangeRateDao
}
