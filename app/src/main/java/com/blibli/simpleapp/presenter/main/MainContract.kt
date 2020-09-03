package com.blibli.simpleapp.presenter.main

import com.blibli.simpleapp.base.BasePresenter
import com.blibli.simpleapp.base.BaseView

interface MainContract {

    interface Presenter : BasePresenter {

        fun onSearchQuerySubmitted(query: String)
    }

    interface View : BaseView<Presenter> {

        fun showUserFragment(query: String)
    }
}