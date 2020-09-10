package com.blibli.simpleapp.feature.user.presenter.user

import com.blibli.simpleapp.core.base.BasePresenter
import com.blibli.simpleapp.feature.user.view.user.UserViewContract

interface UserPresenterContract : BasePresenter<UserViewContract> {

    fun initData(id: Int, username: String)

    fun fetchSearchResults()

    fun fetchByCategory(category: String, log: String)

}
