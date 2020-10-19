package com.blibli.simpleapp.feature.user.view.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.blibli.simpleapp.R
import com.blibli.simpleapp.core.base.BaseActivity
import com.blibli.simpleapp.core.util.ToastHelper
import com.blibli.simpleapp.feature.service.BackgroundService
import com.blibli.simpleapp.feature.service.BoundService
import com.blibli.simpleapp.feature.service.ForegroundService
import com.blibli.simpleapp.feature.user.model.enums.ApiCall
import com.blibli.simpleapp.feature.user.view.savedUser.SavedUserActivity
import com.blibli.simpleapp.feature.user.view.user.UserFragment
import com.blibli.simpleapp.feature.user.viewmodel.MainViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject


class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var intentService: Intent
    private lateinit var intentFgService: Intent
    private lateinit var intentBoundService: Intent

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private lateinit var svUser: SearchView
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNotificationChannel()

        initVar()
        intentService = Intent(this, BackgroundService::class.java)
        intentFgService = Intent(this, ForegroundService::class.java)
        intentBoundService = Intent(this, BoundService::class.java)

        viewModel.query.observe(this, {
            it?.let {
                showUserFragment(it)
            }
        })

        val boundService = BoundService()
        boundService.randomNumberLiveData.observe(this, {
            ToastHelper.showShort(this, "Random number: $it")
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(viewModel.serviceConnection)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.options -> {
                startActivity(Intent(applicationContext, SavedUserActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
                query?.let { query ->
                    hideKeyboard()
                                        startBoundService()
                    //                    startService(intentService)
//                    startForegroundService(intentFgService)
//                    startService(intentFgService)
//                    viewModel.onSearchQuerySubmitted(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun startBoundService() {
        bindService(intentBoundService, viewModel.serviceConnection, Context.BIND_AUTO_CREATE)
        startService(intentBoundService)
    }

    private fun setupNotificationChannel() {
        val channel = NotificationChannel(
            ForegroundService.CHANNEL_ID,
            ForegroundService.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}