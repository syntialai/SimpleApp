package com.blibli.simpleapp.presenter.detail

import com.blibli.simpleapp.base.BasePresenter
import com.blibli.simpleapp.base.BaseView
import com.blibli.simpleapp.data.User

interface DetailContract {

    interface Presenter : BasePresenter {

        fun fetchData(username: String)
    }

    interface View : BaseView<Presenter> {

        fun putDataToUI(data: User)
    }
}