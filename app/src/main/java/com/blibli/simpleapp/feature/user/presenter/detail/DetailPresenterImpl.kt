package com.blibli.simpleapp.feature.user.presenter.detail

import android.util.Log
import com.blibli.simpleapp.core.di.scope.UserScope
import com.blibli.simpleapp.core.network.service.UserService
import com.blibli.simpleapp.core.util.RxHelper.ioToMain
import com.blibli.simpleapp.feature.user.model.User
import com.blibli.simpleapp.feature.user.view.detail.DetailViewContract
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@UserScope
class DetailPresenterImpl @Inject constructor(private var service: UserService) :
    DetailPresenterContract {

    private lateinit var view: DetailViewContract
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
                    data?.let { view.putDataToUI(it) }
                }
            })
    }

    override fun onDestroy() {
        disposable?.dispose()
    }

    override fun injectView(view: DetailViewContract) {
        this.view = view
    }

    companion object {
        private const val FETCH_USER_FAILED = "FETCH USER FAILED"
    }
}
