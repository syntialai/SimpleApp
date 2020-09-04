package com.blibli.simpleapp.feature.user.presenter.detail

import android.util.Log
import com.blibli.simpleapp.core.network.service.UserService
import com.blibli.simpleapp.core.util.RxHelper.ioToMain
import com.blibli.simpleapp.feature.user.model.User
import com.blibli.simpleapp.feature.user.view.detail.DetailViewContract
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import javax.inject.Inject

// (dependencyInjector: DependencyInjector)
class DetailPresenterImpl @Inject constructor(view: DetailViewContract) : DetailPresenterContract {

    @Inject
    lateinit var service: UserService

    private var view: DetailViewContract? = view
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
