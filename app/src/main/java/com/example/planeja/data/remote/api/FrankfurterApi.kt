package com.example.planeja.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query

data class FrankfurterLatestResponse(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)

interface FrankfurterApi {
    @GET("latest")
    suspend fun getLatestRates(
        @Query("from") from: String = "USD",
        @Query("to") to: String = "BRL,EUR"
    ): FrankfurterLatestResponse
}
