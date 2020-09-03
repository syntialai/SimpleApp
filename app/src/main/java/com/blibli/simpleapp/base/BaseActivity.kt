package com.blibli.simpleapp.base

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.blibli.simpleapp.presenter.main.MainContract

abstract class BaseActivity : AppCompatActivity(), MainContract.View {

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val inputManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}