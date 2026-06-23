package com.example.currencyxpro.data.remote

import com.example.currencyxpro.data.remote.model.HistoricalRatesResponse
import com.example.currencyxpro.data.remote.model.LatestRatesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FrankfurterApi {
    @GET("latest")
    suspend fun getLatestRates(
        @Query("base") base: String,
        @Query("symbols") symbols: String? = null
    ): LatestRatesResponse

    @GET("currencies")
    suspend fun getCurrencies(): Map<String, String>

    @GET("{startDate}..{endDate}")
    suspend fun getHistoricalRates(
        @Path("startDate") startDate: String,
        @Path("endDate") endDate: String,
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ): HistoricalRatesResponse
}
