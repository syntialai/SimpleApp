package com.blibli.simpleapp.ui

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.blibli.simpleapp.R
import com.blibli.simpleapp.base.BaseActivity
import com.blibli.simpleapp.data.enums.ApiCall
import com.blibli.simpleapp.presenter.main.MainContract
import com.blibli.simpleapp.presenter.main.impl.MainPresenter

class MainActivity : BaseActivity() {

    private lateinit var svUser: SearchView
    private lateinit var fragmentManager: FragmentManager

    private var username: String = ""

    internal lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager = supportFragmentManager

        svUser = findViewById(R.id.sv_search_user)
        setPresenter(MainPresenter(this))

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

        if (savedInstanceState != null) {
            val savedQuery = savedInstanceState.getString(QUERY)
            savedQuery?.let {
                svUser.setQuery(it, true)
                showUserFragment(it)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(QUERY, username)
        super.onSaveInstanceState(outState)
    }

    override fun showUserFragment(query: String) {
        val userFragment = UserFragment
            .newInstance(1, ApiCall.FETCH_SEARCH_RESULTS.ordinal, query)

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

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    companion object {
        private const val QUERY = "QUERY"
    }
}