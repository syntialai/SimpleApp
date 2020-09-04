package com.blibli.simpleapp.core.di.module

import com.blibli.simpleapp.core.network.service.UserService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class UserModule {

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

//    @Provides
//    @UserScope
//    fun provideDisposable(): Disposable? = null
}