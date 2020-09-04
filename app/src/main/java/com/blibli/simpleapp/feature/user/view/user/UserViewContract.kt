package com.blibli.simpleapp.feature.user.view.user

import com.blibli.simpleapp.core.base.BaseView
import com.blibli.simpleapp.feature.user.model.User
import com.blibli.simpleapp.feature.user.presenter.user.UserPresenterContract

interface UserViewContract : BaseView<UserPresenterContract> {

    fun setAdapter(userList: ArrayList<User>)
}