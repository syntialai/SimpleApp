package com.blibli.simpleapp.feature.user.presenter.user

import com.blibli.simpleapp.core.base.BasePresenter

interface UserPresenterContract : BasePresenter {

    fun initData(id: Int, username: String)

    fun fetchSearchResults(username: String)

    fun fetchFollowingData(username: String)

    fun fetchFollowersData(username: String)
}
