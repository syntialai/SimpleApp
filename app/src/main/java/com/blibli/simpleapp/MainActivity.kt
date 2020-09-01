package com.blibli.simpleapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.blibli.futurekotlin.builder.RetrofitClient
import com.blibli.simpleapp.data.User
import com.blibli.simpleapp.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var svUser: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        svUser = findViewById(R.id.sv_search_user)
        svUser.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    hideKeyboard(svUser)
                    searchData(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun searchData(query: String) {
        RetrofitClient.createService()
            .getUsers(query)
            .enqueue(object: Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body() as UserResponse
                        val usersList = data.items
                        usersList?.let {
                            showUserFragment(it)
                        }
                    } else {
                        Log.d(SEARCH_FAILED, response.toString())
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d(SEARCH_FAILED, t.toString())
                }
            })
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

    private fun showUserFragment(usersList: ArrayList<User>) {
        val userFragment = UserFragment.newInstance(1, usersList)

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager
            .beginTransaction()

        fragmentTransaction.add(
            R.id.fl_container_users,
            userFragment
        ).addToBackStack(null).commit()
    }

    companion object {
        const val SEARCH_FAILED = "SEARCH FAILED"
    }
}