package com.blibli.simpleapp.core.base

import android.view.View

interface BaseView<T> {

    fun initVar(view: View? = null)
}