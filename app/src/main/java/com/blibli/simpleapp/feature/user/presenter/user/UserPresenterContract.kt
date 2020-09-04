package com.blibli.simpleapp.feature.user.presenter.user

import com.blibli.simpleapp.core.base.BasePresenter
import com.blibli.simpleapp.feature.user.view.user.UserViewContract

interface UserPresenterContract : BasePresenter<UserViewContract> {

    fun initData(id: Int, username: String)

    fun fetchSearchResults(username: String)

    fun fetchFollowingData(username: String)

    fun fetchFollowersData(username: String)
}
