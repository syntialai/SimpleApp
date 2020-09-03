package com.blibli.simpleapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blibli.simpleapp.R
import com.blibli.simpleapp.adapter.UserAdapter
import com.blibli.simpleapp.data.User
import com.blibli.simpleapp.data.enums.ApiCall
import com.blibli.simpleapp.di.impl.DependencyInjectorImpl
import com.blibli.simpleapp.presenter.user.UserContract
import com.blibli.simpleapp.presenter.user.impl.UserPresenter
import com.google.android.material.textview.MaterialTextView

class UserFragment : Fragment(), UserContract.View {

    private var columnCount = 1
    private var apiId = ApiCall.FETCH_SEARCH_RESULTS.ordinal
    private var username: String? = null

    private var adapter = UserAdapter()

    private lateinit var rvUsers: RecyclerView
    private lateinit var tvNoUsers: MaterialTextView
    private lateinit var presenter: UserContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setPresenter(UserPresenter(this, DependencyInjectorImpl()))

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            apiId = it.getInt(ARG_API_ID)
            username = it.getString(ARG_USER_NAME)
            username?.let { name ->
                presenter.initData(apiId, name)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_users, container, false)

        tvNoUsers = view.findViewById(R.id.tv_no_users)
        rvUsers = view.findViewById(R.id.rv_users)
        rvUsers.layoutManager = if (columnCount == 1) {
            LinearLayoutManager(context)
        } else {
            GridLayoutManager(context, columnCount)
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

    override fun setAdapter(userList: ArrayList<User>) {
        adapter.updateList(userList)
        showRecyclerView(userList.size > 0)
    }

    override fun setPresenter(presenter: UserContract.Presenter) {
        this.presenter = presenter
    }

    private fun showRecyclerView(show: Boolean) {
        rvUsers.visibility = if (show) View.VISIBLE else View.GONE
        tvNoUsers.visibility = if (show) View.GONE else View.VISIBLE
    }

    companion object {
        const val ARG_USER_NAME = "USER_NAME"

        private const val ARG_COLUMN_COUNT = "COLUMN_COUNT"
        private const val ARG_API_ID = "API_ID"

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