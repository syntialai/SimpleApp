package com.blibli.simpleapp.presenter.main.impl

import com.blibli.simpleapp.presenter.main.MainContract

class MainPresenter(view: MainContract.View) : MainContract.Presenter {

    private var view: MainContract.View? = view

    override fun onSearchQuerySubmitted(query: String) {
        view?.showUserFragment(query)
    }

    override fun onDestroy() {
        this.view = null
    }
}