package com.blibli.simpleapp.feature.user.view.detail

import com.blibli.simpleapp.core.base.BaseView
import com.blibli.simpleapp.feature.user.model.User
import com.blibli.simpleapp.feature.user.presenter.detail.DetailPresenterContract

interface DetailViewContract : BaseView<DetailPresenterContract> {

    fun putDataToUI(data: User)
}
