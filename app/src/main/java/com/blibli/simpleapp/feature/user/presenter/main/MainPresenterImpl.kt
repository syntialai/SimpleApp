package com.blibli.simpleapp.feature.user.presenter.main

import com.blibli.simpleapp.feature.user.view.main.MainViewContract
import javax.inject.Inject

class MainPresenterImpl @Inject constructor() : MainPresenterContract {

    private lateinit var view: MainViewContract

    override fun onSearchQuerySubmitted(query: String) {
        view.showUserFragment(query)
    }

    override fun onDestroy() {}

    override fun injectView(view: MainViewContract) {
        this.view = view
    }
}