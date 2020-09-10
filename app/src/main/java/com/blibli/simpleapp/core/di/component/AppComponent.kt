package com.blibli.simpleapp.core.di.component

import com.blibli.simpleapp.SimpleApp
import com.blibli.simpleapp.core.di.module.*
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        NetModule::class,
        ActivityModule::class,
        FragmentModule::class,
        ViewModelFactoryModule::class
    ]
)
interface AppComponent {

    fun inject(application: SimpleApp)
}
