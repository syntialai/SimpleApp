package com.blibli.simpleapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blibli.futurekotlin.builder.RetrofitClient
import com.blibli.simpleapp.data.User
import com.blibli.simpleapp.response.UserResponse
import com.blibli.simpleapp.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFragment : Fragment() {

    private var columnCount = 1
    private var apiId = 0
    private var username: String? = null

    private var userList = ArrayList<User>()
    private var adapter = UserAdapter()

    private lateinit var rvUsers: RecyclerView
    private lateinit var service: UserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        service = RetrofitClient.createService()

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            apiId = it.getInt(ARG_API_ID)
            username = it.getString(ARG_USER_NAME)
            initData()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_users, container, false)

        rvUsers = view.findViewById(R.id.rv_users)
        rvUsers.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }
        rvUsers.adapter = adapter

        adapter.setOnItemClickedCallback(object: UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(ARG_USER_NAME, data.login)
                startActivity(intent)
            }
        })

        return view
    }

    private fun setAdapter() {
        adapter.updateList(userList)
    }

    private fun initData() {
        when (apiId) {
            0 -> fetchSearchResults()
            1 -> fetchFollowingData()
            2 -> fetchFollowersData()
        }
    }

    private fun fetchSearchResults() {
        username?.let {
            service
                .getUsers(it)
                .enqueue(object: Callback<UserResponse> {
                    override fun onResponse(
                        call: Call<UserResponse>,
                        response: Response<UserResponse>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body() as UserResponse
                            val usersList = data.items
                            usersList?.let {
                                userList = it
                                setAdapter()
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
    }

    private fun fetchFollowingData() {
        username?.let {
            service.getUserByUsernameAndCategory(it, "following")
                .enqueue(object: Callback<ArrayList<User>> {
                    override fun onResponse(
                        call: Call<ArrayList<User>>,
                        response: Response<ArrayList<User>>
                    ) {
                        if (response.isSuccessful) {
                            userList = response.body() as ArrayList<User>
                            setAdapter()
                        } else {
                            Log.d(FETCH_FOLLOWING_FAILED, response.toString())
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                        Log.d(FETCH_FOLLOWING_FAILED, t.toString())
                    }
                })
        }
    }

    private fun fetchFollowersData() {
        username?.let {
            service.getUserByUsernameAndCategory(it, "followers")
                .enqueue(object: Callback<ArrayList<User>> {
                    override fun onResponse(
                        call: Call<ArrayList<User>>,
                        response: Response<ArrayList<User>>
                    ) {
                        if (response.isSuccessful) {
                            userList = response.body() as ArrayList<User>
                            setAdapter()
                        } else {
                            Log.d(FETCH_FOLLOWERS_FAILED, response.toString())
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                        Log.d(FETCH_FOLLOWERS_FAILED, t.toString())
                    }
                })
        }
    }

    companion object {
        const val ARG_COLUMN_COUNT = "COLUMN_COUNT"
        const val ARG_API_ID = "API_ID"
        const val ARG_USER_NAME = "USER_NAME"
        const val FETCH_FOLLOWING_FAILED = "FETCH FOLLOWING_FAILED"
        const val FETCH_FOLLOWERS_FAILED = "FETCH FOLLOWERS_FAILED"
        const val SEARCH_FAILED = "SEARCH FAILED"

        @JvmStatic
        fun newInstance(columnCount: Int, id: Int, username: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putInt(ARG_API_ID, id)
                    putString(ARG_USER_NAME, username)
                }
            }
    }
}