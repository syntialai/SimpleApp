package com.blibli.simpleapp.feature.user.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blibli.simpleapp.core.network.service.UserService
import com.blibli.simpleapp.core.util.RxHelper.ioToMain
import com.blibli.simpleapp.feature.user.db.repository.UserRepository
import com.blibli.simpleapp.feature.user.model.User
import com.blibli.simpleapp.feature.user.model.enums.ApiCall
import com.blibli.simpleapp.feature.user.model.response.UserResponse
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private var service: UserService,
    private var repository: UserRepository
) : ViewModel() {

    private var _data: MutableLiveData<ArrayList<User>> = MutableLiveData(arrayListOf())
    val data: LiveData<ArrayList<User>>
        get() = _data

    private val _username = MutableLiveData<String?>()
    val username: LiveData<String?>
        get() = _username

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isInserted = MutableLiveData<Int>()
    val isInserted: LiveData<Int>
        get() = _isInserted

    private val _id = MutableLiveData<Int>()

    private val _columnCount = MutableLiveData<Int>()
    val columnCount: LiveData<Int>
        get() = _columnCount

    private val _page = MutableLiveData<Int>()
    private val _perPage = MutableLiveData<Int>()
    private val _disposable = MutableLiveData<Disposable?>()

    init {
        _id.value = 0
        _columnCount.value = 1
        _page.value = 1
        _perPage.value = 10
        _isInserted.value = -1
        _isLoading.value = true
        _disposable.value = null
    }

    fun initData(id: Int?, username: String?, column: Int?) {
        _columnCount.value = column
        _username.value = username
        _id.value = id

        callApiFetch()
    }

    private fun fetchSearchResults() {
        _username.value?.let {
            service.getUsers(it, _page.value!!, _perPage.value!!)
                .ioToMain()
                .subscribe(object : Observer<UserResponse> {
                    override fun onSubscribe(d: Disposable) {
                        _disposable.value = d
                        _isLoading.value = true
                    }

                    override fun onNext(t: UserResponse) {
                        t.items?.let { list ->
                            addToList(list, (_page.value!! > 1))
                        }
                        Log.d("USER LIST", _data.value.toString())
                    }

                    override fun onError(e: Throwable) {
                        Log.d(SEARCH_FAILED, e.toString())
                        _isLoading.value = false
                    }

                    override fun onComplete() {
                        _isLoading.value = false
                    }
                })
        }
    }

    fun onDestroy() {
        _disposable.value?.dispose()
    }

    fun loadMore() {
        _page.value = (_page.value ?: 1) + 1
        _isLoading.value = true
        _data.value?.let { list ->
            list.add(User())
            _isInserted.value = list.size - 1
        }

        callApiFetch()
    }

    private fun callApiFetch() {
        _isInserted.value = -1

        when (_id.value) {
            ApiCall.FETCH_SEARCH_RESULTS.ordinal -> fetchSearchResults()
            ApiCall.FETCH_FOLLOWING_DATA.ordinal -> fetchByCategory(
                "following",
                FETCH_FOLLOWING_FAILED
            )
            ApiCall.FETCH_FOLLOWERS_DATA.ordinal -> fetchByCategory(
                "followers",
                FETCH_FOLLOWERS_FAILED
            )
            ApiCall.FETCH_DB.ordinal -> fetchDb()
        }
    }

    private fun fetchDb() {
        repository.getUsers()
            .ioToMain()
            .subscribe(object: Observer<List<com.blibli.simpleapp.feature.user.db.model.User>>{
                override fun onSubscribe(d: Disposable) {
                    _disposable.value = d
                }

                override fun onNext(t: List<com.blibli.simpleapp.feature.user.db.model.User>) {
                    val list = t.map {
                        it.toUserModule()
                    } as ArrayList<User>

                    addToList(list, false)
                }

                override fun onError(e: Throwable) {
                    Log.d("ERROR ROOM", e.toString())
                    _isLoading.value = false
                }

                override fun onComplete() {
                    Log.d("isLoading ${_isLoading.value}", _data.value.toString())
                    _isLoading.value = false
                }
            })
    }

    fun addToList(list: ArrayList<User>, update: Boolean) {
        _data.value?.let { dataList ->
            if (update) {
                dataList.removeAt(dataList.size - 1)
            } else {
                dataList.clear()
            }

            dataList.addAll(list)
            Log.d("USER LIST", dataList.toString())
        }

        _isLoading.value = false
    }

    private fun fetchByCategory(category: String, log: String) {
        username.value?.let {
            service
                .getUserByUsernameAndCategory(it, category, _page.value!!, _perPage.value!!)
                .ioToMain()
                .subscribe(object : Observer<ArrayList<User>> {
                    override fun onSubscribe(d: Disposable) {
                        _disposable.value = d
                    }

                    override fun onNext(t: ArrayList<User>) {
                        addToList(t, (_page.value!! > 1))
                    }

                    override fun onError(e: Throwable) {
                        Log.d(log, e.toString())
                        _isLoading.value = false
                    }

                    override fun onComplete() {
                        _isLoading.value = false
                    }
                })
        }
    }

    private fun com.blibli.simpleapp.feature.user.db.model.User.toUserModule(): User {

        return User(
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
        const val FETCH_FOLLOWING_FAILED = "FETCH FOLLOWING_FAILED"
        const val FETCH_FOLLOWERS_FAILED = "FETCH FOLLOWERS_FAILED"
        const val SEARCH_FAILED = "SEARCH FAILED"
    }
}
