package com.blibli.simpleapp.feature.user.presenter.main

import com.blibli.simpleapp.feature.user.view.main.MainViewContract

class MainPresenterImpl(view: MainViewContract) : MainPresenterContract {

    private var view: MainViewContract? = view

    override fun onSearchQuerySubmitted(query: String) {
        view?.showUserFragment(query)
    }

    override fun onDestroy() {
        this.view = null
    }
}