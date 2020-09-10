package com.blibli.simpleapp.feature.user.view.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.blibli.simpleapp.R
import com.blibli.simpleapp.core.base.BaseActivity
import com.blibli.simpleapp.feature.user.model.enums.ApiCall
import com.blibli.simpleapp.feature.user.view.user.UserFragment
import com.blibli.simpleapp.feature.user.viewmodel.MainViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private lateinit var svUser: SearchView
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initVar()

        viewModel.query.observe(this, {
            it?.let {
                showUserFragment(it)
            }
        })
    }

    private fun showUserFragment(query: String) {
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

    private fun initVar() {
        fragmentManager = supportFragmentManager

        svUser = findViewById(R.id.sv_search_user)
        svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    hideKeyboard()
                    viewModel.onSearchQuerySubmitted(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }
}