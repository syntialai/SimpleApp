package com.blibli.simpleapp.presenter.detail.impl

import android.util.Log
import com.blibli.simpleapp.data.User
import com.blibli.simpleapp.di.DependencyInjector
import com.blibli.simpleapp.presenter.detail.DetailContract
import com.blibli.simpleapp.util.RxHelper.ioToMain
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class DetailPresenter(view: DetailContract.View, dependencyInjector: DependencyInjector) :
    DetailContract.Presenter {

    private val service = dependencyInjector.userService()

    private var view: DetailContract.View? = view
    private var data: User? = null
    private var disposable: Disposable? = null

    override fun fetchData(username: String) {
        service
            .getUserByUsername(username)
            .ioToMain()
            .subscribe(object : Observer<User> {
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(t: User) {
                    data = t
                }

                override fun onError(e: Throwable) {
                    Log.d(FETCH_USER_FAILED, e.toString())
                }

                override fun onComplete() {
                    data?.let { view?.putDataToUI(it) }
                }
            })
    }

    override fun onDestroy() {
        disposable?.dispose()
        this.view = null
    }

    companion object {
        private const val FETCH_USER_FAILED = "FETCH USER FAILED"
    }
}
