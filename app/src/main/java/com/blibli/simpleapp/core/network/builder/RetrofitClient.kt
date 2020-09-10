package com.blibli.simpleapp.core.network.builder

import com.blibli.simpleapp.core.network.service.UserService
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    const val BASE_URL = "https://api.github.com/"
    const val TOKEN = "97594694f9d152352bb26dc38901039c5b231bbf"
    private var client: OkHttpClient
    private var rxAdapter: RxJava2CallAdapterFactory

    init {
        val interceptor = Interceptor { chain ->
            val request = chain.request().newBuilder().addHeader(
                "Authorization",
                "token $TOKEN"
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
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(rxAdapter)
            .client(client)
            .build()

        return retrofit.create(UserService::class.java)
    }
}