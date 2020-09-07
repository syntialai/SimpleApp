package com.blibli.simpleapp.core.di.component

import com.blibli.simpleapp.SimpleApp
import com.blibli.simpleapp.core.di.module.*
import dagger.Component
import dagger.android.AndroidInjectionModule
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        NetModule::class,
        ActivityModule::class,
        FragmentModule::class,
    ]
)
interface AppComponent {

    fun inject(application: SimpleApp)

//    fun inject(mainActivity: MainActivity)

    // Dependent Components
    // downstream components need these exposed
//    fun retrofit(): Retrofit

    // Subcomponents
    // factory method to instantiate the subcomponent defined here (passing in the module instance)

//    fun userSubcomponent(userModule: UserModule): UserSubcomponent

//    fun subcomponentBuilders(): MutableMap<Class<*>?, Provider<SubcomponentBuilder<*>?>?>?
}
