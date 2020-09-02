package com.blibli.futurekotlin.builder

import com.blibli.simpleapp.service.UserService
import com.squareup.moshi.Moshi
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


object RetrofitClient {
    private const val BASE_URL = "https://api.github.com/"
    private var client: OkHttpClient
    private var rxAdapter: RxJava2CallAdapterFactory

    // GSON
    private val gsonConverter = GsonConverterFactory.create()

    // Jackson
    private val jacksonConverter = JacksonConverterFactory.create()

    // Moshi
    private val moshi = Moshi.Builder().build()
    private val moshiConverter = MoshiConverterFactory.create(moshi)

    init {
        val interceptor = Interceptor { chain ->
            val request = chain.request().newBuilder().addHeader(
                "Authorization",
                "token 8cc409e10334e3b1f2d7aac50ae490aa4e0965bc"
            ).build()
            chain.proceed(request)
        }

        val builder = OkHttpClient.Builder()
        builder.interceptors().add(interceptor)
        client = builder.build()

        rxAdapter = RxJava2CallAdapterFactory
            .createWithScheduler(Schedulers.io())
    }

    fun createService(): UserService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(gsonConverter)
            .addCallAdapterFactory(rxAdapter)
            .client(client)
            .build()

        return retrofit.create(UserService::class.java)
    }
}