package com.blibli.simpleapp.core.util

import android.content.Context
import android.widget.Toast

object ToastHelper {

    fun showShort(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}