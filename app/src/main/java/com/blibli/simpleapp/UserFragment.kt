package com.blibli.simpleapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blibli.simpleapp.dummy.DummyContent

class UserFragment : Fragment() {

    private var columnCount = 1
    private var userList = ArrayList<User>()
    private lateinit var rvUsers: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        initData()
        Log.d("FRAGMENT", userList.toString())
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

        val adapter = UserAdapter(userList)
        rvUsers.adapter = adapter

        adapter.setOnItemClickedCallback(object: UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                // Do nothing
            }
        })

        return view
    }

    private fun initData() {
        userList = arrayListOf(
            User("Syntia", "https://avatars3.githubusercontent.com/u/53588149?v=4")
        )
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}