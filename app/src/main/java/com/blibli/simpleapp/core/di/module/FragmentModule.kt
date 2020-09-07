package com.blibli.simpleapp.core.di.module

import com.blibli.simpleapp.core.di.scope.UserScope
import com.blibli.simpleapp.feature.user.view.user.UserFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

//@Module(subcomponents = [FragmentSubcomponent::class])
@Module
abstract class FragmentModule {

//    @Binds
//    @IntoMap
//    @ClassKey(Fragment::class)
//    abstract fun bindFragmentFactory(factory: FragmentSubcomponent.Factory)
//            : AndroidInjector.Factory<Fragment>

    // Generate automatically when no method or supertypes other than Factory
    @UserScope
    @ContributesAndroidInjector(modules = [UserModule::class])
    abstract fun contributeUserFragment(): UserFragment
}

