package com.blibli.simpleapp.feature.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.blibli.simpleapp.core.util.ToastHelper
import java.util.*

class BoundService : Service() {

    private val binder = MyBinder()

    private val generator: Random = Random()

    private var generateRandomNumber: Boolean = false

    val randomNumberLiveData: MutableLiveData<Int> = MutableLiveData()

    override fun onCreate() {
        super.onCreate()
        ToastHelper.showShort(applicationContext, "Bound service is running!")

        generateRandomNumber = true
        while (generateRandomNumber) {
            val randomNumber = generator.nextInt(100)
            randomNumberLiveData.value = randomNumber
        }
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onDestroy() {
        super.onDestroy()
        generateRandomNumber = false
        ToastHelper.showShort(applicationContext, "Bound service is stopped!")
    }

    inner class MyBinder : Binder() {

        val service: BoundService
            get() = this@BoundService
    }
}