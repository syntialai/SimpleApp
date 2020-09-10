package com.blibli.simpleapp.core.di.module

import androidx.lifecycle.ViewModelProvider
import com.blibli.simpleapp.core.di.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory?): ViewModelProvider.Factory?
}