package com.blibli.simpleapp.feature.user.view.savedUser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.blibli.simpleapp.R
import com.blibli.simpleapp.feature.user.model.enums.ApiCall
import com.blibli.simpleapp.feature.user.view.user.UserFragment

class SavedUserActivity : AppCompatActivity() {

    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_user)
        title = "Saved Users"

        fragmentManager = supportFragmentManager
        showUserFragment()
    }

    private fun showUserFragment() {
        val userFragment = UserFragment.newInstance(1, ApiCall.FETCH_DB.ordinal, "Dummy")

        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(
            R.id.fl_container_users,
            userFragment
        ).commit()
    }
}