package com.blibli.simpleapp.core.base

interface BasePresenter<T> {

    fun onDestroy()

    fun injectView(view: T)
}
