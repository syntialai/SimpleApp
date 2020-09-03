package com.blibli.simpleapp.util

import android.content.Context

object ResourcesHelper {

    fun getDimen(context: Context, dimenId: Int): Int {
        return context.resources.getDimensionPixelSize(dimenId)
    }
}
