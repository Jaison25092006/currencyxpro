package com.example.currencyxpro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_currencies")
data class FavoriteCurrencyEntity(
    @PrimaryKey val code: String,
    val name: String,
    val symbol: String
)
