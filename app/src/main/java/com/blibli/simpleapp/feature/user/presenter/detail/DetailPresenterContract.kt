package com.blibli.simpleapp.feature.user.presenter.detail

import com.blibli.simpleapp.core.base.BasePresenter

interface DetailPresenterContract : BasePresenter {

    fun fetchData(username: String)
}
