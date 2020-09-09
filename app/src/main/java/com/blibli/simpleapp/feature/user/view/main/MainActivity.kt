package com.blibli.simpleapp.feature.user.view.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.blibli.simpleapp.R
import com.blibli.simpleapp.core.base.BaseActivity
import com.blibli.simpleapp.feature.user.model.enums.ApiCall
import com.blibli.simpleapp.feature.user.presenter.main.MainPresenterImpl
import com.blibli.simpleapp.feature.user.view.user.UserFragment
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : BaseActivity() {

    private lateinit var svUser: SearchView
    private lateinit var fragmentManager: FragmentManager

    private var username: String = ""

    @Inject
    lateinit var presenter: MainPresenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        presenter.injectView(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initVar()
        initState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(QUERY, username)
        super.onSaveInstanceState(outState)
    }

    override fun showUserFragment(query: String) {
        val userFragment = UserFragment.newInstance(1, ApiCall.FETCH_SEARCH_RESULTS.ordinal, query)

        val fragmentTransaction: FragmentTransaction = fragmentManager
            .beginTransaction()

        if (fragmentManager.findFragmentById(R.id.fl_container_users) != null) {
            fragmentManager.popBackStack()
        }

        fragmentTransaction.add(
            R.id.fl_container_users,
            userFragment
        ).addToBackStack(null).commit()
    }

    override fun initVar(view: View?) {
        fragmentManager = supportFragmentManager

        svUser = findViewById(R.id.sv_search_user)
        svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    hideKeyboard()
                    username = it
                    presenter.onSearchQuerySubmitted(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun initState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val savedQuery = savedInstanceState.getString(QUERY)
            savedQuery?.let {
                svUser.setQuery(it, true)
                showUserFragment(it)
            }
        }
    }

    companion object {
        private const val QUERY = "QUERY"
    }
}