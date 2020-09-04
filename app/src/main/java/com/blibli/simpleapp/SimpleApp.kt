package com.blibli.simpleapp

import android.app.Application
import com.blibli.simpleapp.core.di.component.AppComponent
import com.blibli.simpleapp.core.di.component.DaggerAppComponent
import com.blibli.simpleapp.core.di.component.DaggerUserComponent
import com.blibli.simpleapp.core.di.component.UserComponent
import com.blibli.simpleapp.core.di.module.AppModule
import com.blibli.simpleapp.core.di.module.NetModule
import com.blibli.simpleapp.core.di.module.UserModule
import com.blibli.simpleapp.core.network.builder.RetrofitClient
import retrofit2.Retrofit

class SimpleApp : Application() {

    private lateinit var mAppComponent: AppComponent
    private lateinit var mUserComponent: UserComponent

    override fun onCreate() {
        super.onCreate()

        mAppComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .netModule(NetModule("https://api.github.com/", RetrofitClient.BASE_URL))
            .build()

        mUserComponent = DaggerUserComponent.builder()
            .appComponent(mAppComponent)
            .userModule(UserModule())
            .build()
    }

    fun getAppComponent(): AppComponent = mAppComponent

    fun getUserComponent(): UserComponent = mUserComponent
}