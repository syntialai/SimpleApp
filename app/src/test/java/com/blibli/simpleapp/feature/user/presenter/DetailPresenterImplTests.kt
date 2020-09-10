package com.blibli.simpleapp.feature.user.presenter

import com.blibli.simpleapp.core.RxSchedulerRule
import com.blibli.simpleapp.core.network.service.UserService
import com.blibli.simpleapp.feature.user.model.User
import com.blibli.simpleapp.feature.user.presenter.detail.DetailPresenterImpl
import com.blibli.simpleapp.feature.user.view.detail.DetailViewContract
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.junit.Assert.*
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailPresenterImplTests {

    @InjectMocks
    private lateinit var detailPresenter: DetailPresenterImpl

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var detailView: DetailViewContract

    @Mock
    private var disposable: Disposable? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        detailPresenter.injectView(detailView)
    }

    @Test
    fun fetchData_SuccessFetchDataFromAPI() {
        val dummyUsername = ArgumentMatchers.anyString()
        val dummyUser = User("dummy_user")

        val expectedData = Observable.just(dummyUser)
        `when`(userService.getUserByUsername(dummyUsername))
            .thenReturn(expectedData)

        detailPresenter.fetchData(dummyUsername)
        val data = getUserList()

        assertNotNull(disposable)
        assertNotNull(data)
        assertEquals(dummyUser, data)
        data?.let { verify(detailView).putDataToUI(it) }
    }

    @Test(expected = Throwable::class)
    fun fetchData_ErrorFetchDataFromAPI() {
        val dummyUsername = ArgumentMatchers.anyString()

        `when`(userService.getUserByUsername(dummyUsername))
            .thenThrow(Throwable())

        detailPresenter.fetchData(dummyUsername)
        val data = getUserList()

        assertNull(disposable)
        assertNull(data)
    }

    @Test
    fun onDestroy_CallsDisposeToDisposable() {
        `when`(disposable?.isDisposed).thenReturn(true)

        detailPresenter.onDestroy()

        verify(disposable)?.dispose()
        disposable?.isDisposed?.let {
            assertTrue(it)
        }
    }

    private fun getUserList(): User? {
        val data = detailPresenter.javaClass.getDeclaredField("data")
        data.isAccessible = true
        return data.get(detailPresenter) as User?
    }

    private fun setUsername(user: String) {
        val username = detailPresenter
            .javaClass
            .getDeclaredField("username")
        username.isAccessible = true
        username.set(detailPresenter, user)
    }

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxSchedulerRule()
    }

}
