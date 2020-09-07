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

//    private lateinit var mAppComponent: AppComponent
//    private lateinit var mUserComponent: UserComponent

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

//        With AndroidInjector Builder
        DaggerAppComponent.builder()
            .netModule(
                NetModule(
                    RetrofitClient.BASE_URL,
                    RetrofitClient.TOKEN
                )
            )
            .build()
            .inject(this)

//        Basic builder
//        mAppComponent = DaggerAppComponent.builder()
//            .appModule(AppModule(this))
//            .netModule(
//                NetModule(
//                    RetrofitClient.BASE_URL,
//                    RetrofitClient.TOKEN
//                )
//            )
//            .build()

//        Define User dependent component
//        mUserComponent = DaggerUserComponent.builder()
//            .appComponent(mAppComponent)
//            .userModule(UserModule())
//            .build()
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

//    fun getAppComponent(): AppComponent = mAppComponent

//    fun getUserComponent(): UserComponent = mUserComponent
}