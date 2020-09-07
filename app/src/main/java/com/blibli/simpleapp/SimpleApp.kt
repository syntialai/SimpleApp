package com.blibli.simpleapp

import android.app.Application
import com.blibli.simpleapp.core.di.component.AppComponent
import com.blibli.simpleapp.core.di.component.DaggerAppComponent
import com.blibli.simpleapp.core.di.module.AppModule
import com.blibli.simpleapp.core.di.module.NetModule
import com.blibli.simpleapp.core.network.builder.RetrofitClient

class SimpleApp : Application() {

    private lateinit var mAppComponent: AppComponent
//    private lateinit var mUserComponent: UserComponent

    override fun onCreate() {
        super.onCreate()

        mAppComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .netModule(NetModule(
                RetrofitClient.BASE_URL,
                RetrofitClient.TOKEN
            ))
            .build()

//        Define User dependent component
//        mUserComponent = DaggerUserComponent.builder()
//            .appComponent(mAppComponent)
//            .userModule(UserModule())
//            .build()
    }

    fun getAppComponent(): AppComponent = mAppComponent

//    fun getUserComponent(): UserComponent = mUserComponent
}