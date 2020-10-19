package com.blibli.simpleapp.feature.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.blibli.simpleapp.R
import com.blibli.simpleapp.core.util.ToastHelper
import com.blibli.simpleapp.feature.user.view.main.MainActivity
import java.util.*


class ForegroundService : Service() {

    companion object {
        const val CHANNEL_ID = "foreground_service_channel"
        const val CHANNEL_NAME = "Foreground Service Channel"
        private const val NOTIFICATION_ID = 1001
    }

    override fun onCreate() {
        super.onCreate()

        foregroundNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(this.toString(), "Foreground service !")
        ToastHelper.showShort(applicationContext, "Foreground service is running")

        foregroundNotification()

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy();
        ToastHelper.showShort(applicationContext, "Foreground service is stopped!")
        stopForeground(true)
    }

    override fun onBind(intent: Intent): IBinder? = null

    private fun foregroundNotification() {

//        val notificationIntent = Intent(this, MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Foreground service is Running")
            .setSmallIcon(R.drawable.ic_github)
//            .setContentIntent(pendingIntent)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }
}