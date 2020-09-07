package com.blibli.simpleapp.feature.user.view.user

import android.content.Context
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
import com.blibli.simpleapp.feature.user.adapter.UserAdapter
import com.blibli.simpleapp.feature.user.model.User
import com.blibli.simpleapp.feature.user.model.enums.ApiCall
import com.blibli.simpleapp.feature.user.presenter.user.UserPresenterImpl
import com.blibli.simpleapp.feature.user.view.detail.DetailActivity
import com.google.android.material.textview.MaterialTextView
import dagger.android.AndroidInjection
import javax.inject.Inject

class UserFragment : Fragment(), UserViewContract {

    private var columnCount = 1
    private var apiId = ApiCall.FETCH_SEARCH_RESULTS.ordinal
    private var username: String? = null

    private var adapter = UserAdapter()

    private lateinit var rvUsers: RecyclerView
    private lateinit var tvNoUsers: MaterialTextView

    @Inject
    lateinit var presenter: UserPresenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            apiId = it.getInt(ARG_API_ID)
            username = it.getString(ARG_USER_NAME)
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

        username?.let { name ->
            presenter.initData(apiId, name)
        }
        presenter.injectView(this)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //        With AndroidInjector
        AndroidInjection.inject(activity)

//        User dependent component
//        (context.applicationContext as SimpleApp).getUserComponent().inject(this)

//        User subcomponent
//        (context.applicationContext as SimpleApp).getAppComponent()
//            .userSubcomponent(UserModule())
//            .inject(this)
    }

    override fun setAdapter(userList: ArrayList<User>) {
        adapter.updateList(userList)
        showRecyclerView(userList.size > 0)
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