package com.blibli.simpleapp.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.blibli.simpleapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var svUser: SearchView
    private lateinit var fragmentManager: FragmentManager

    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager = supportFragmentManager

        svUser = findViewById(R.id.sv_search_user)
        svUser.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    hideKeyboard(svUser)
                    username = it
                    showUserFragment(it)
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

    private fun hideKeyboard(view: View) {
        val inputManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    private fun showUserFragment(query: String) {
        val userFragment = UserFragment.newInstance(1, 0, query)

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

    companion object {
        private const val QUERY = "QUERY"
    }
}