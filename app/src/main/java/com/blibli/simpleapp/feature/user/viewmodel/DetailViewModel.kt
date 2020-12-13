package com.blibli.simpleapp.feature.user.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blibli.simpleapp.feature.user.db.model.User
import com.blibli.simpleapp.feature.user.db.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailViewModel @ViewModelInject constructor(
    private var userRepository: UserRepository
) : ViewModel() {

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    private val _username = MutableLiveData<String>()
    val username: LiveData<String>
        get() = _username

    private val _isLoadingData = MutableLiveData<Boolean>()
    val isLoadingData: LiveData<Boolean>
        get() = _isLoadingData

    private val _data = MutableLiveData<User>()
    val data: LiveData<User>
        get() = _data

    init {
        _isLoadingData.value = true
        _data.value = User()
    }

    fun fetchData(username: String) {
        _username.value = username

        launchDataLoad {
            userRepository.showUser(username).collect {
                _data.value = it
            }
        }
    }

    private fun launchDataLoad(block: suspend () -> Unit) {
        coroutineScope.launch {
            try {
                _isLoadingData.value = true
                block()
            } catch (error: Throwable) {
                Log.d(FETCH_USER_FAILED, error.toString())
            } finally {
                _isLoadingData.value = false
            }
        }
    }

    companion object {
        private const val FETCH_USER_FAILED = "FETCH USER FAILED"
    }
}
