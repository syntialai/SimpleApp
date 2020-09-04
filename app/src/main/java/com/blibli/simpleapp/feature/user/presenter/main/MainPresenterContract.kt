package com.blibli.simpleapp.feature.user.presenter.main

import com.blibli.simpleapp.core.base.BasePresenter

interface MainPresenterContract : BasePresenter {

    fun onSearchQuerySubmitted(query: String)
}
