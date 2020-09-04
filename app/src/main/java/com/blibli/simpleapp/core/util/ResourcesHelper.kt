package com.blibli.simpleapp.core.util

import android.content.Context

object ResourcesHelper {

    fun getDimen(context: Context, dimenId: Int): Int {
        return context.resources.getDimensionPixelSize(dimenId)
    }

    fun getString(context: Context, stringId: Int): String {
        return context.resources.getString(stringId)
    }
}
