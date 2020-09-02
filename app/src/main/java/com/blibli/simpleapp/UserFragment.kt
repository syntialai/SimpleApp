package com.blibli.simpleapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blibli.futurekotlin.builder.RetrofitClient
import com.blibli.simpleapp.data.User
import com.blibli.simpleapp.response.UserResponse
import com.blibli.simpleapp.service.UserService
import com.google.android.material.textview.MaterialTextView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UserFragment : Fragment() {

    private var columnCount = 1
    private var apiId = 0
    private var username: String? = null

    private var userList = ArrayList<User>()
    private var adapter = UserAdapter()
    private var disposable: Disposable? = null

    private lateinit var rvUsers: RecyclerView
    private lateinit var tvNoUsers: MaterialTextView
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

        tvNoUsers = view.findViewById(R.id.tv_no_users)
        rvUsers = view.findViewById(R.id.rv_users)
        rvUsers.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }
        rvUsers.adapter = adapter

        adapter.setOnItemClickedCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(ARG_USER_NAME, data.login)
                startActivity(intent)
            }
        })

        return view
    }

    override fun onDestroy() {
        disposable?.dispose()
        Log.d("isDisposed", disposable?.isDisposed.toString())
        super.onDestroy()
    }

    private fun setAdapter() {
        adapter.updateList(userList)
        showRecyclerView(userList.size > 0)
    }

    private fun showRecyclerView(show: Boolean) {
        rvUsers.visibility = if (show) View.VISIBLE else View.GONE
        tvNoUsers.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun initData() {
        when (apiId) {
            0 -> fetchSearchResults()
            1 -> fetchFollowingData()
            2 -> fetchFollowersData()
        }
    }

    private fun fetchSearchResults() {
        username?.let { username ->
            val call = service.getUsers(username)
            call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserResponse> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: UserResponse) {
                        t.items?.let { list -> userList = list }
                    }

                    override fun onError(e: Throwable) {
                        Log.d(SEARCH_FAILED, e.toString())
                    }

                    override fun onComplete() {
                        setAdapter()
                    }
                })
        }
    }

    private fun fetchFollowingData() {
        username?.let {
            val call = service
                .getUserByUsernameAndCategory(it, "following")
            call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<User>> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: ArrayList<User>) {
                        userList = t
                    }

                    override fun onError(e: Throwable) {
                        Log.d(FETCH_FOLLOWING_FAILED, e.toString())
                    }

                    override fun onComplete() {
                        setAdapter()
                    }
                })
        }
    }

    private fun fetchFollowersData() {
        username?.let {
            val call = service.getUserByUsernameAndCategory(it, "followers")
            call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<User>> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: ArrayList<User>) {
                        userList = t
                    }

                    override fun onError(e: Throwable) {
                        Log.d(FETCH_FOLLOWERS_FAILED, e.toString())
                    }

                    override fun onComplete() {
                        setAdapter()
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