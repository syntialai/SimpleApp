package com.blibli.simpleapp.feature.user.presenter

//import androidx.lifecycle.MutableLiveData
//import com.blibli.simpleapp.core.network.service.UserService
//import com.blibli.simpleapp.feature.user.model.User
//import com.blibli.simpleapp.feature.user.model.response.UserResponse
//import com.blibli.simpleapp.feature.user.presenter.user.UserPresenterImpl
//import com.blibli.simpleapp.feature.user.view.user.UserViewContract
//import io.reactivex.Observable
//import io.reactivex.android.plugins.RxAndroidPlugins
//import io.reactivex.disposables.Disposable
//import io.reactivex.schedulers.Schedulers
//import junit.framework.Assert.*
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.ArgumentMatchers
//import org.mockito.InjectMocks
//import org.mockito.Mock
//import org.mockito.Mockito.*
//import org.mockito.MockitoAnnotations
//import org.mockito.junit.MockitoJUnitRunner
//
//@RunWith(MockitoJUnitRunner::class)
//class UserPresenterImplTests {
//
//    @InjectMocks
//    private lateinit var userPresenter: UserPresenterImpl
//
//    @Mock
//    private lateinit var userService: UserService
//
//    @Mock
//    private lateinit var userView: UserViewContract
//
//    @Mock
//    private var disposable: Disposable? = null
//
//    private val page = 1
//    private val perPage = 10
//
//    @Before
//    fun setup() {
//        MockitoAnnotations.initMocks(this)
//        userPresenter.injectView(userView)
//        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
//    }
//
//    @Test
//    fun fetchSearchResults_SuccessFetchDataFromAPI() {
//        val dummyUsername = "dummy_username"
//        val dummyUserList = arrayListOf(User(dummyUsername))
//        val dummyUserResponse = UserResponse(
//            1,
//            false,
//            dummyUserList
//        )
//
//        setUsername(dummyUsername)
//
//        val expectedData = Observable.just(dummyUserResponse)
//        `when`(userService.getUsers(dummyUsername, page, perPage))
//            .thenReturn(expectedData)
//
//        userPresenter.fetchSearchResults()
//
//        val data = userPresenter.data.value
//
//        assertNotNull(disposable)
//        assertNotNull(data)
////        assertEquals(dummyUserList, data)
//        assertFalse(getIsLoading())
//        verify(userView).setAdapter(data!!)
//    }
//
//    @Test(expected = Throwable::class)
//    fun fetchSearchResults_ErrorFetchDataFromAPI() {
//        val dummyUsername = ArgumentMatchers.anyString()
//
//        setUsername(dummyUsername)
//
//        `when`(userService.getUsers(dummyUsername, page, perPage))
//            .thenThrow(Throwable())
//
//        userPresenter.fetchSearchResults()
//        val data = userPresenter.data.value
//
//        assertNull(disposable)
//        assertNull(data)
//    }
//
//    @Test
//    fun fetchByCategory_SuccessFetchDataFromAPI() {
//        val dummyUsername = "Dummy_username"
//        val dummyCategory = "Category"
//        val dummyLog = "LOG"
//        val dummyUserList = arrayListOf(User(dummyUsername))
//
//        setUsername(dummyUsername)
//
//        val expectedData = Observable.just(dummyUserList)
//        `when`(
//            userService.getUserByUsernameAndCategory(
//                dummyUsername,
//                dummyCategory,
//                page,
//                perPage
//            )
//        ).thenReturn(expectedData)
//
//        userPresenter.fetchByCategory(dummyCategory, dummyLog)
//        val data = userPresenter.data.value
//
//        assertNotNull(disposable)
//        assertNotNull(data)
////        assertEquals(dummyUserList, data)
//        assertFalse(getIsLoading())
//        verify(userView).setAdapter(data!!)
//    }
//
//    @Test(expected = Throwable::class)
//    fun fetchByCategory_ErrorFetchDataFromAPI() {
//        val dummyUsername = "Dummy_username"
//        val dummyCategory = "Category"
//        val dummyLog = "LOG"
//
//        setUsername(dummyUsername)
//
//        `when`(
//            userService.getUserByUsernameAndCategory(
//                dummyUsername,
//                dummyCategory,
//                page,
//                perPage
//            )
//        ).thenThrow(Throwable())
//
//        userPresenter.fetchByCategory(dummyCategory, dummyLog)
//        val data = userPresenter.data.value
//
//        assertNull(disposable)
//        assertNull(data)
//    }
//
//    @Test
//    fun loadMore_CallsCallApiFetchFun() {
////        `when`(userPresenter.callApiFetch()).thenCallRealMethod()
//
//        val expectedPage = page + 1
//        val expectedPosition = 0
//
//        setId(-1)
//        userPresenter.loadMore()
//
//        val isLoading = getIsLoading()
//        val page = getPage()
//
//        assertTrue(isLoading)
//        assertEquals(expectedPage, page)
//        verify(userView).notifyItemInserted(expectedPosition)
////        verify(userPresenter).callApiFetch()
//    }
//
//    @Test
//    fun callApiFetch_WhenIdIs0_ThenCallFetchSearchResultsFun() {
//        val dummyUsername = "dummy_username"
//        val dummyUserList = arrayListOf(User(dummyUsername))
//        val dummyUserResponse = UserResponse(
//            1,
//            false,
//            dummyUserList
//        )
//
//        setUsername(dummyUsername)
//
//        val expectedData = Observable.just(dummyUserResponse)
//        `when`(userService.getUsers(dummyUsername, page, perPage))
//            .thenReturn(expectedData)
//
////        `when`(userPresenter.fetchSearchResults()).then {
////            fetchSearchResults_SuccessFetchDataFromAPI()
////        }
//
//        setId(0)
//
//        userPresenter.callApiFetch()
//
//        assertEquals(0, getId())
////        verify(userPresenter).fetchSearchResults()
//    }
//
//    @Test
//    fun callApiFetch_WhenIdIs1_ThenCallFetchFollowingDataFun() {
////        `when`(userPresenter.fetchFollowingData()).then {
////            fetchByCategory_SuccessFetchDataFromAPI()
////        }
//        val dummyUsername = "Dummy_username"
//        val dummyCategory = "following"
//        val dummyUserList = arrayListOf(User(dummyUsername))
//
//        setUsername(dummyUsername)
//
//        val expectedData = Observable.just(dummyUserList)
//        `when`(
//            userService.getUserByUsernameAndCategory(
//                dummyUsername,
//                dummyCategory,
//                page,
//                perPage
//            )
//        ).thenReturn(expectedData)
//
//        setId(1)
//
//        userPresenter.callApiFetch()
//
//        assertEquals(1, getId())
//    }
//
//    @Test
//    fun callApiFetch_WhenIdIs2_ThenCallFetchFollowersDataFun() {
//        val dummyUsername = "Dummy_username"
//        val dummyCategory = "followers"
//        val dummyUserList = arrayListOf(User(dummyUsername))
//
//        setUsername(dummyUsername)
//
//        val expectedData = Observable.just(dummyUserList)
//        `when`(
//            userService.getUserByUsernameAndCategory(
//                dummyUsername,
//                dummyCategory,
//                page,
//                perPage
//            )
//        ).thenReturn(expectedData)
//
//        setId(2)
//
//        userPresenter.callApiFetch()
//
//        assertEquals(2, getId())
////        verify(userPresenter).fetchFollowingData()
//    }
//
//    @Test
//    fun addToList_WhenUpdateIsTrue_ThenUpdateList() {
//        val listToAdd = arrayListOf(User("user"), User("user_2"))
//        val list = arrayListOf(User("user_0"), User())
//
//        val expectedData = arrayListOf(User("user_0"))
//        expectedData.addAll(listToAdd)
//        setData(list)
//
//        userPresenter.addToList(listToAdd, true)
//
//        assertEquals(expectedData, userPresenter.data.value)
//    }
//
//    @Test
//    fun addToList_WhenUpdateIsFalse_ThenOverwriteList() {
//        val expectedData = arrayListOf(User("user"), User("user_2"))
//        val list = arrayListOf(User())
//        setData(list)
//
//        userPresenter.addToList(expectedData, false)
//
//        assertEquals(expectedData, userPresenter.data.value)
//    }
//
//    @Test
//    fun onDestroy_CallsDisposeToDisposable() {
//        `when`(disposable?.isDisposed).thenReturn(true)
//
//        userPresenter.onDestroy()
//
//        verify(disposable)?.dispose()
//        disposable?.isDisposed?.let {
//            assertTrue(it)
//        }
//    }
//
//    private fun setId(i: Int) {
//        val id = userPresenter.javaClass.getDeclaredField("id")
//        id.isAccessible = true
//        id.setInt(userPresenter, i)
//    }
//
//    private fun setUsername(user: String) {
//        val username = userPresenter
//            .javaClass
//            .getDeclaredField("username")
//        username.isAccessible = true
//        username.set(userPresenter, user)
//    }
//
//    private fun setData(list: ArrayList<User>) {
//        val data = userPresenter.javaClass.getDeclaredField("_data")
//        data.isAccessible = true
//        data.set(userPresenter, MutableLiveData(list))
//    }
//
//    private fun getId() : Int {
//        val id = userPresenter.javaClass.getDeclaredField("id")
//        id.isAccessible = true
//        return id.getInt(userPresenter)
//    }
//
//    private fun getIsLoading(): Boolean {
//        val isLoading = userPresenter.javaClass.getDeclaredField("isLoading")
//        isLoading.isAccessible = true
//        return isLoading.getBoolean(userPresenter)
//    }
//
//    private fun getPage(): Int {
//        val page = userPresenter.javaClass.getDeclaredField("page")
//        page.isAccessible = true
//        return page.getInt(userPresenter)
//    }
//}