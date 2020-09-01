package com.blibli.futurekotlin.builder

import com.blibli.simpleapp.service.UserService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    const val BASE_URL = "https://api.github.com/"

    fun createService(): UserService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(UserService::class.java)
    }
}