package com.blibli.simpleapp.core.di.module

import com.blibli.simpleapp.core.di.scope.UserScope
import com.blibli.simpleapp.feature.user.view.detail.DetailActivity
import com.blibli.simpleapp.feature.user.view.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

//@Module(subcomponents = [ActivitySubcomponent::class])
@Module
abstract class ActivityModule {

//    @Binds
//    @IntoMap
//    @ClassKey(Activity::class)
//    abstract fun bindActivityFactory(factory: ActivitySubcomponent.Factory)
//            : AndroidInjector.Factory<Activity>

    // Generate automatically when no method or supertypes other than Factory
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @UserScope
    @ContributesAndroidInjector(modules = [UserModule::class])
    abstract fun contributeDetailActivity(): DetailActivity
}
