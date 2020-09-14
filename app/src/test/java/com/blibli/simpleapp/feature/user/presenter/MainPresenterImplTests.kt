package com.blibli.simpleapp.feature.user.presenter

//import com.blibli.simpleapp.feature.user.presenter.main.MainPresenterImpl
//import com.blibli.simpleapp.feature.user.view.main.MainViewContract
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.ArgumentMatchers
//import org.mockito.InjectMocks
//import org.mockito.Mock
//import org.mockito.Mockito.verify
//import org.mockito.MockitoAnnotations
//import org.mockito.junit.MockitoJUnitRunner
//
//@RunWith(MockitoJUnitRunner::class)
//class MainPresenterImplTests {
//
//    @InjectMocks
//    private lateinit var mainPresenter: MainPresenterImpl
//
//    @Mock(name = "view")
//    private lateinit var mainView: MainViewContract
//
//    @Before
//    fun setup() {
//        MockitoAnnotations.initMocks(this)
//        mainPresenter.injectView(mainView)
//    }
//
//    @Test
//    fun onDestroy_DoNothing() {
//        mainPresenter.onDestroy()
//    }
//
//    @Test
//    fun onSearchQuerySubmitted_CallsViewShowUserFragment() {
//        val dummyQuery = ArgumentMatchers.anyString()
//
//        mainPresenter.onSearchQuerySubmitted(dummyQuery)
//
//        verify(mainView).showUserFragment(dummyQuery)
//    }
//}
