package com.blibli.simpleapp.presenter.user

import com.blibli.simpleapp.base.BasePresenter
import com.blibli.simpleapp.base.BaseView
import com.blibli.simpleapp.data.User

interface UserContract {

    interface Presenter : BasePresenter {

        fun initData(id: Int, username: String)

        fun fetchSearchResults(username: String)

        fun fetchFollowingData(username: String)

        fun fetchFollowersData(username: String)
    }

    interface View : BaseView<Presenter> {

        fun setAdapter(userList: ArrayList<User>)
    }
}