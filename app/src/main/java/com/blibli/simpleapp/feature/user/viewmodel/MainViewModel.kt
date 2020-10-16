package com.blibli.simpleapp.feature.user.viewmodel

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blibli.simpleapp.feature.service.BoundService
import javax.inject.Inject


class MainViewModel @Inject constructor() : ViewModel() {

    private val _query = MutableLiveData<String?>()
    val query: LiveData<String?>
        get() = _query

    val binder: MutableLiveData<BoundService.MyBinder> = MutableLiveData()

    private val _serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(this.toString(), "ServiceConnection: connected to service.")
            val mBinder: BoundService.MyBinder = service as BoundService.MyBinder
            binder.value = mBinder
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(this.toString(), "ServiceConnection: disconnected from service.")
            binder.value = null
        }
    }
    val serviceConnection: ServiceConnection
        get() = _serviceConnection

    fun onSearchQuerySubmitted(query: String) {
        _query.value = query
    }
}
