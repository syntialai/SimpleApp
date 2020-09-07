package com.blibli.simpleapp.core.di.component

import com.blibli.simpleapp.core.di.module.UserModule
import com.blibli.simpleapp.core.di.scope.UserScope
import com.blibli.simpleapp.feature.user.view.detail.DetailActivity
import com.blibli.simpleapp.feature.user.view.user.UserFragment
import dagger.Subcomponent

@UserScope
@Subcomponent(modules = [UserModule::class])
interface UserSubcomponent {

    fun inject(activity: DetailActivity)

    fun inject(fragment: UserFragment)

//    Builder to separate subcomponent from parent component
//    @Subcomponent.Builder
//    interface Builder : SubcomponentBuilder<UserSubcomponent?> {
//        fun activityModule(module: UserModule?): Builder?
//    }
}
