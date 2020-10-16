package com.blibli.simpleapp.feature.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.blibli.simpleapp.core.util.ToastHelper


class BackgroundService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(this.toString(), "Background service !")
        ToastHelper.showShort(applicationContext, "Background service is running in..")

        runThreadSleep()

        return START_STICKY
    }

    override fun onDestroy() {
        ToastHelper.showShort(applicationContext, "Background service is stopped!")
    }

    override fun onBind(intent: Intent): IBinder? = null

    private fun runThreadSleep() {
        for (i in 1..10) {
            try {
                Thread.sleep(1000)
                ToastHelper.showShort(applicationContext, i.toString())
            } catch (e: Exception) {
                Log.e("MyService", "Error", e)
            }
        }
        stopSelf()
    }
}