package com.blibli.simpleapp.presenter.user.impl

import android.util.Log
import com.blibli.simpleapp.data.User
import com.blibli.simpleapp.data.enums.ApiCall
import com.blibli.simpleapp.data.response.UserResponse
import com.blibli.simpleapp.di.DependencyInjector
import com.blibli.simpleapp.presenter.user.UserContract
import com.blibli.simpleapp.util.RxHelper.ioToMain
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UserPresenter(view: UserContract.View, dependencyInjector: DependencyInjector) :
    UserContract.Presenter {

    private val service = dependencyInjector.userService()

    private var view: UserContract.View? = view
    private var data: ArrayList<User> = arrayListOf()
    private var disposable: Disposable? = null

    override fun initData(id: Int, username: String) {
        when (id) {
            ApiCall.FETCH_SEARCH_RESULTS.ordinal -> fetchSearchResults(username)
            ApiCall.FETCH_FOLLOWING_DATA.ordinal -> fetchFollowingData(username)
            ApiCall.FETCH_FOLLOWERS_DATA.ordinal -> fetchFollowersData(username)
        }
    }

    override fun fetchSearchResults(username: String) {
        service.getUsers(username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<UserResponse> {
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(t: UserResponse) {
                    t.items?.let { list -> data = list }
                }

                override fun onError(e: Throwable) {
                    Log.d(SEARCH_FAILED, e.toString())
                }

                override fun onComplete() {
                    view?.setAdapter(data)
                }
            })
    }

    override fun fetchFollowingData(username: String) {
        fetchByCategory(username, "following", FETCH_FOLLOWING_FAILED)
    }

    override fun fetchFollowersData(username: String) {
        fetchByCategory(username, "followers", FETCH_FOLLOWERS_FAILED)
    }

    override fun onDestroy() {
        disposable?.dispose()
        this.view = null
    }

    private fun fetchByCategory(username: String, category: String, log: String) {
        service
            .getUserByUsernameAndCategory(username, category)
            .ioToMain()
            .subscribe(object : Observer<ArrayList<User>> {
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(t: ArrayList<User>) {
                    data = t
                }

                override fun onError(e: Throwable) {
                    Log.d(log, e.toString())
                }

                override fun onComplete() {
                    view?.setAdapter(data)
                }
            })
    }

    companion object {
        private const val FETCH_FOLLOWING_FAILED = "FETCH FOLLOWING_FAILED"
        private const val FETCH_FOLLOWERS_FAILED = "FETCH FOLLOWERS_FAILED"
        private const val SEARCH_FAILED = "SEARCH FAILED"
    }
}
