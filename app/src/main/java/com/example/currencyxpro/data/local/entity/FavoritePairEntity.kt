package com.example.currencyxpro.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "favorite_pairs",
    primaryKeys = ["baseCurrency", "targetCurrency"]
)
data class FavoritePairEntity(
    val baseCurrency: String,
    val targetCurrency: String
)
