package com.blibli.simpleapp.di.impl

import com.blibli.simpleapp.di.DependencyInjector
import com.blibli.simpleapp.network.builder.RetrofitClient
import com.blibli.simpleapp.network.service.UserService

class DependencyInjectorImpl : DependencyInjector {

    override fun userService(): UserService {
        return RetrofitClient.createService()
    }
}