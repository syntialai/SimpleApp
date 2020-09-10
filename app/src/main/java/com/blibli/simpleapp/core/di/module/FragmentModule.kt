package com.blibli.simpleapp.core.di.module

import com.blibli.simpleapp.core.di.scope.UserScope
import com.blibli.simpleapp.feature.user.view.user.UserFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    // Generate automatically when no method or supertypes other than Factory
    @UserScope
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeUserFragment(): UserFragment
}

