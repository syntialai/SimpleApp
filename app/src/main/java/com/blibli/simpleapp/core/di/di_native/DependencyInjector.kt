package com.blibli.simpleapp.core.di.di_native

import com.blibli.simpleapp.core.network.service.UserService

interface DependencyInjector {

    fun userService(): UserService
}