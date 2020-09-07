package com.blibli.simpleapp.core.di.component

import com.blibli.simpleapp.core.di.module.AppModule
import com.blibli.simpleapp.core.di.module.NetModule
import com.blibli.simpleapp.core.di.module.UserModule
import com.blibli.simpleapp.feature.user.view.main.MainActivity
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class])
interface AppComponent {

    fun inject(main: MainActivity)

    // Dependent Components
    // downstream components need these exposed
    fun retrofit(): Retrofit

    // Subcomponents
    // factory method to instantiate the subcomponent defined here (passing in the module instance)
    fun userSubcomponent(userModule: UserModule): UserSubcomponent
//    fun subcomponentBuilders(): MutableMap<Class<*>?, Provider<SubcomponentBuilder<*>?>?>?
}
