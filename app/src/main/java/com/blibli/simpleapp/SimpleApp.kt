package com.blibli.simpleapp

import android.app.Application
import com.blibli.simpleapp.core.di.component.DaggerAppComponent
import com.blibli.simpleapp.core.di.module.AppModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class SimpleApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
            .inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}
