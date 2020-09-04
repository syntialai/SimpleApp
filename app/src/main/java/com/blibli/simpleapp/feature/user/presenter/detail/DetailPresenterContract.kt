package com.blibli.simpleapp.feature.user.presenter.detail

import com.blibli.simpleapp.core.base.BasePresenter
import com.blibli.simpleapp.feature.user.view.detail.DetailViewContract

interface DetailPresenterContract : BasePresenter<DetailViewContract> {

    fun fetchData(username: String)
}
