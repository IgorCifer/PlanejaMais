package com.example.planeja.data.remote

import com.example.planeja.data.remote.api.FrankfurterApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RemoteModule {

    private val retrofitFrankfurter: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.frankfurter.app/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val frankfurterApi: FrankfurterApi =
        retrofitFrankfurter.create(FrankfurterApi::class.java)
}
