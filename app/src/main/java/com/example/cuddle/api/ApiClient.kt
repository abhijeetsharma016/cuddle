package com.example.cuddle.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private var retrofit: Retrofit? = null

    fun getRetrofitInstance(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl("https://fcm.papayacoders.in")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
}
