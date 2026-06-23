package com.example.currencyxpro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyxpro.data.local.entity.ExchangeRateEntity

@Dao
interface ExchangeRateDao {
    @Query("SELECT * FROM exchange_rates WHERE baseCurrency = :base")
    suspend fun getRatesForBase(base: String): List<ExchangeRateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<ExchangeRateEntity>)

    @Query("DELETE FROM exchange_rates")
    suspend fun clearCachedRates()
}
