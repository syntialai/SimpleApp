package com.blibli.simpleapp

import android.app.Application
import com.blibli.simpleapp.core.di.component.DaggerAppComponent
import com.blibli.simpleapp.core.di.module.NetModule
import com.blibli.simpleapp.core.network.builder.RetrofitClient
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
            .netModule(
                NetModule(
                    RetrofitClient.BASE_URL,
                    RetrofitClient.TOKEN
                )
            )
            .build()
            .inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}
