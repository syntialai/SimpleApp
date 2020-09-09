package com.blibli.simpleapp.feature.user.presenter.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blibli.simpleapp.core.di.scope.UserScope
import com.blibli.simpleapp.core.network.service.UserService
import com.blibli.simpleapp.core.util.RxHelper.ioToMain
import com.blibli.simpleapp.feature.user.model.User
import com.blibli.simpleapp.feature.user.model.enums.ApiCall
import com.blibli.simpleapp.feature.user.model.response.UserResponse
import com.blibli.simpleapp.feature.user.view.user.UserViewContract
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@UserScope
class UserPresenterImpl @Inject constructor(private var service: UserService) :
    UserPresenterContract {

    private lateinit var view: UserViewContract

    private var _data: MutableLiveData<ArrayList<User>> = MutableLiveData(arrayListOf())
    val data: LiveData<ArrayList<User>>
        get() = _data

    private var username: String = ""
    private var id: Int = 0
    private var page: Int = 1
    private var perPage: Int = 10
    private var isLoading: Boolean = false
    private var disposable: Disposable? = null

    override fun initData(id: Int, username: String) {
        this.username = username
        this.id = id

        callApiFetch()
    }

    override fun fetchSearchResults() {
        service.getUsers(username, page, perPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<UserResponse> {
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(t: UserResponse) {
                    t.items?.let { list ->
                        addToList(list, (page > 1))
                    }
                    Log.d("USER LIST", _data.value.toString())
                }

                override fun onError(e: Throwable) {
                    Log.d(SEARCH_FAILED, e.toString())
                }

                override fun onComplete() {
                    _data.value?.let { view.setAdapter(it) }
                    isLoading = false
                }
            })
    }

    override fun fetchFollowingData() {
        fetchByCategory("following", FETCH_FOLLOWING_FAILED)
    }

    override fun fetchFollowersData() {
        fetchByCategory("followers", FETCH_FOLLOWERS_FAILED)
    }

    override fun onDestroy() {
        disposable?.dispose()
    }

    override fun injectView(view: UserViewContract) {
        this.view = view
    }

    fun loadMore() {
        page++
        isLoading = true
        _data.value?.let { list ->
            list.add(User())
            view.notifyItemInserted(list.size - 1)
        }

        callApiFetch()
    }

    private fun callApiFetch() {
        when (id) {
            ApiCall.FETCH_SEARCH_RESULTS.ordinal -> fetchSearchResults()
            ApiCall.FETCH_FOLLOWING_DATA.ordinal -> fetchFollowingData()
            ApiCall.FETCH_FOLLOWERS_DATA.ordinal -> fetchFollowersData()
        }
    }

    private fun addToList(list: ArrayList<User>, update: Boolean) {
        if (update) {
            _data.value?.let { dataList ->
                dataList.removeAt(dataList.size - 1)
                dataList.addAll(list)
                Log.d("USER LIST", dataList.toString())
            }
        } else {
            _data.value = list
            Log.d("USER LIST", _data.value.toString())
        }
    }

    private fun fetchByCategory(category: String, log: String) {
        service
            .getUserByUsernameAndCategory(username, category, page, perPage)
            .ioToMain()
            .subscribe(object : Observer<ArrayList<User>> {
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(t: ArrayList<User>) {
                    addToList(t, (page > 1))
                }

                override fun onError(e: Throwable) {
                    Log.d(log, e.toString())
                }

                override fun onComplete() {
                    _data.value?.let { view.setAdapter(it) }
                    isLoading = false
                }
            })
    }

    companion object {
        private const val FETCH_FOLLOWING_FAILED = "FETCH FOLLOWING_FAILED"
        private const val FETCH_FOLLOWERS_FAILED = "FETCH FOLLOWERS_FAILED"
        private const val SEARCH_FAILED = "SEARCH FAILED"
    }
}
