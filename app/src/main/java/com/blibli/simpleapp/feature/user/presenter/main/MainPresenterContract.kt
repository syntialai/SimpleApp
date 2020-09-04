package com.blibli.simpleapp.feature.user.presenter.main

import com.blibli.simpleapp.core.base.BasePresenter
import com.blibli.simpleapp.feature.user.view.main.MainViewContract

interface MainPresenterContract : BasePresenter<MainViewContract> {

    fun onSearchQuerySubmitted(query: String)
}
