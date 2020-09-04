package com.blibli.simpleapp.core.di.component

import com.blibli.simpleapp.core.di.module.UserModule
import com.blibli.simpleapp.core.di.scope.UserScope
import com.blibli.simpleapp.core.network.service.UserService
import com.blibli.simpleapp.feature.user.view.detail.DetailActivity
import com.blibli.simpleapp.feature.user.view.user.UserFragment
import dagger.Component
import dagger.Subcomponent

@UserScope
@Component(dependencies = [AppComponent::class], modules = [UserModule::class])
interface UserComponent {

    fun userService(): UserService
}
