package com.blibli.simpleapp.feature.user.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blibli.simpleapp.feature.user.db.model.User
import com.blibli.simpleapp.feature.user.db.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private var repository: UserRepository
) : ViewModel() {

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
            val user = repository.showUser(username)
            _data.value = user
        }
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
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
