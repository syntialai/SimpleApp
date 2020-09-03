package com.blibli.simpleapp.di

import com.blibli.simpleapp.network.service.UserService

interface DependencyInjector {

    fun userService(): UserService
}