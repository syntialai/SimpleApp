package com.blibli.simpleapp.core.di.di_native.impl

import com.blibli.simpleapp.core.di.di_native.DependencyInjector
import com.blibli.simpleapp.core.network.builder.RetrofitClient
import com.blibli.simpleapp.core.network.service.UserService

class DependencyInjectorImpl : DependencyInjector {

    override fun userService(): UserService {
        return RetrofitClient.createService()
    }
}