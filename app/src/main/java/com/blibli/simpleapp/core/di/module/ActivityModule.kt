package com.blibli.simpleapp.core.di.module

import com.blibli.simpleapp.core.di.scope.UserScope
import com.blibli.simpleapp.feature.user.view.detail.DetailActivity
import com.blibli.simpleapp.feature.user.view.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @UserScope
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @UserScope
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeDetailActivity(): DetailActivity
}
