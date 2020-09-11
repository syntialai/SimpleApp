package com.blibli.simpleapp.feature.user.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blibli.simpleapp.core.network.service.UserService
import com.blibli.simpleapp.core.util.RxHelper.ioToMain
import com.blibli.simpleapp.feature.user.db.repository.UserRepository
import com.blibli.simpleapp.feature.user.model.User
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private var service: UserService,
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

    private var disposable = MutableLiveData<Disposable?>()

    init {
        _isLoadingData.value = true
        _data.value = User()
        disposable.value = null
    }

    fun fetchData(username: String) {
        _username.value = username

        service
            .getUserByUsername(username)
            .ioToMain()
            .subscribe(object : Observer<User> {
                override fun onSubscribe(d: Disposable) {
                    disposable.value = d
                    _isLoadingData.value = true
                }

                override fun onNext(t: User) {
                    _data.value = t
                    viewModelScope.launch(Dispatchers.IO) {
                        repository.addUser(t.toUserModule())
                    }
                }

                override fun onError(e: Throwable) {
                    Log.d(FETCH_USER_FAILED, e.toString())
                    _isLoadingData.value = false
                }

                override fun onComplete() {
                    _isLoadingData.value = false
                }
            })
    }

    fun onDestroy() {
        disposable.value?.dispose()
    }

    private fun User.toUserModule(): com.blibli.simpleapp.feature.user.db.model.User {

        return com.blibli.simpleapp.feature.user.db.model.User(
            null,
            this.login,
            this.avatar_url,
            this.public_repos,
            this.followers,
            this.following,
            this.followers_url,
            this.following_url
        )
    }

    companion object {
        private const val FETCH_USER_FAILED = "FETCH USER FAILED"
    }
}
