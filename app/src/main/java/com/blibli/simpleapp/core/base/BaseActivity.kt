package com.blibli.simpleapp.core.base

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.blibli.simpleapp.feature.user.view.main.MainViewContract

abstract class BaseActivity : AppCompatActivity() {

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val inputManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}