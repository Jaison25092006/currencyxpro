package com.example.currencyxpro.data.remote.model

import com.google.gson.annotations.SerializedName

data class LatestRatesResponse(
    @SerializedName("amount") val amount: Double,
    @SerializedName("base") val base: String,
    @SerializedName("date") val date: String,
    @SerializedName("rates") val rates: Map<String, Double>
)
