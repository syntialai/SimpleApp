package com.blibli.simpleapp.feature.user.view.main

import com.blibli.simpleapp.core.base.BaseView
import com.blibli.simpleapp.feature.user.presenter.main.MainPresenterContract

interface MainViewContract : BaseView<MainPresenterContract> {

    fun showUserFragment(query: String)
}