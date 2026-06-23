package com.example.currencyxpro.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "exchange_rates",
    primaryKeys = ["baseCurrency", "targetCurrency"]
)
data class ExchangeRateEntity(
    val baseCurrency: String,
    val targetCurrency: String,
    val rate: Double,
    val timestamp: Long
)
