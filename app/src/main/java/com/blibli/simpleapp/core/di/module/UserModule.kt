package com.blibli.simpleapp.core.di.module

import com.blibli.simpleapp.core.di.scope.UserScope
import com.blibli.simpleapp.core.network.service.UserService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class UserModule {

    @Provides
    @UserScope
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }
}
