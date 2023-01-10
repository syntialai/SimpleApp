package com.blibli.simpleapp.feature.user.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blibli.simpleapp.feature.user.db.repository.UserRepository
import com.blibli.simpleapp.feature.user.model.User
import com.blibli.simpleapp.feature.user.model.enums.ApiCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserViewModel @ViewModelInject constructor(
    private var repository: UserRepository
) : ViewModel() {

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

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

    init {
        _id.value = 0
        _columnCount.value = 1
        _page.value = 1
        _perPage.value = 10
        _isInserted.value = -1
        _isLoading.value = true
    }

    fun initData(id: Int?, username: String?, column: Int?) {
        _columnCount.value = column
        _username.value = username
        _id.value = id

        callApiFetch()
    }

    private fun fetchSearchResults() {
        _username.value?.let {
            launchDataLoad({
                repository.fetchUsers(it, _page.value!!, _perPage.value!!)
                    .collect { response ->
                        response.items?.let { list ->
                            addToList(list, (_page.value!! > 1))
                        }
                    }
            }, SEARCH_FAILED)
        }
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
        launchDataLoad({
            repository.getUsers().collect {
                addToList(it, false)
            }
        }, ROOM_GET_USERS_FAILED)
    }

    private fun addToList(list: ArrayList<User>, update: Boolean) {
        if (update) {
            removeDataLastElm()
        } else {
            clearData()
        }

        _data.value?.addAll(list)
        Log.d(USER_LIST, _data.value.toString())
    }

    private fun removeDataLastElm() {
        _data.value?.let {
            it.removeAt(it.size - 1)
        }
    }

    private fun clearData() {
        _data.value?.clear()
    }

    private fun fetchByCategory(category: String, log: String) {
        username.value?.let {
            launchDataLoad({
                repository.fetchUserByUsernameAndCategory(
                    it,
                    category,
                    _page.value!!,
                    _perPage.value!!
                ).collect {
                    addToList(it, (_page.value!! > 1))
                }
            }, log)
        }
    }

    private fun launchDataLoad(block: suspend () -> Unit, log: String) {
        coroutineScope.launch {
            try {
                _isLoading.value = true
                block()
            } catch (error: Throwable) {
                Log.d(log, error.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }

    companion object {
        const val FETCH_FOLLOWING_FAILED = "FETCH FOLLOWING_FAILED"
        const val FETCH_FOLLOWERS_FAILED = "FETCH FOLLOWERS_FAILED"
        const val SEARCH_FAILED = "SEARCH FAILED"
        const val ROOM_GET_USERS_FAILED = "ROOM_GET_USERS_FAILED"
        const val USER_LIST = "USER LIST"
    }
}
