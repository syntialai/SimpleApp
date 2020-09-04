package com.blibli.simpleapp.core.di.component

import com.blibli.simpleapp.core.di.module.AppModule
import com.blibli.simpleapp.core.di.module.NetModule
import com.blibli.simpleapp.core.network.service.UserService
import com.blibli.simpleapp.feature.user.view.detail.DetailActivity
import com.blibli.simpleapp.feature.user.view.main.MainActivity
import com.blibli.simpleapp.feature.user.view.user.UserFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class])
interface AppComponent {

    fun inject(main: MainActivity)

    fun inject(activity: DetailActivity)

    fun inject(fragment: UserFragment)
}
