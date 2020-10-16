package com.blibli.simpleapp.feature.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.blibli.simpleapp.core.util.ToastHelper
import java.util.*

class BoundService : Service() {

    private val binder = MyBinder()

    private val generator: Random = Random()

    val randomNumberLiveData: MutableLiveData<Int> = MutableLiveData()

    override fun onCreate() {
        super.onCreate()
        Log.i(this.toString(), "Bound service is running!")

        Handler().postDelayed({
            val randomNumber = generator.nextInt(100)
            randomNumberLiveData.postValue(randomNumber)
        }, 1000)
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onDestroy() {
        super.onDestroy()
        ToastHelper.showShort(applicationContext, "Bound service is stopped!")
    }

    inner class MyBinder : Binder() {

        val service: BoundService
            get() = this@BoundService
    }
}