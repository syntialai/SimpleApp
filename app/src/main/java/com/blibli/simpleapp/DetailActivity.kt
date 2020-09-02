package com.blibli.simpleapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.blibli.futurekotlin.builder.RetrofitClient
import com.blibli.simpleapp.data.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textview.MaterialTextView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailActivity : AppCompatActivity() {

    private lateinit var tvUsername: MaterialTextView
    private lateinit var tvFollowing: MaterialTextView
    private lateinit var tvFollowers: MaterialTextView
    private lateinit var tvRepos: MaterialTextView
    private lateinit var ivUserImage: ShapeableImageView

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private var username = ""
    private var data: User? = null
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initView()
        getUsernameIntent()
        fetchData()
        setupViewPager()
        setupTabs()
    }

    override fun onDestroy() {
        disposable?.dispose()
        Log.d("isDisposed", disposable?.isDisposed.toString())
        super.onDestroy()
    }

    private fun initView() {
        tvUsername = findViewById(R.id.tv_user_detail_username)
        tvFollowing = findViewById(R.id.tv_user_detail_following)
        tvFollowers = findViewById(R.id.tv_user_detail_followers)
        tvRepos = findViewById(R.id.tv_user_detail_repos)
        ivUserImage = findViewById(R.id.iv_user_detail_image)

        tabLayout = findViewById(R.id.tab_users)
        viewPager = findViewById(R.id.viewpager_user)
    }

    private fun setupTabs() {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> resources.getString(R.string.following_label)
                else -> resources.getString(R.string.followers_label)
            }
        }.attach()
    }

    private fun setupViewPager() {
        viewPager.adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount(): Int = 2

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> UserFragment.newInstance(1, 1, username)
                    else -> UserFragment.newInstance(1, 2, username)
                }
            }
        }
    }

    private fun getUsernameIntent() {
        val usernameFromIntent = intent.getStringExtra(UserFragment.ARG_USER_NAME)
        usernameFromIntent?.let {
            username = it
        }
    }

    private fun fetchData() {
        val call = RetrofitClient.createService().getUserByUsername(username)
        call
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<User> {
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(t: User) {
                    data = t
                }

                override fun onError(e: Throwable) {
                    Log.d(FETCH_USER_FAILED, e.toString())
                }

                override fun onComplete() {
                    putDataToUI()
                }
            })
    }

    private fun putDataToUI() {
        val context = applicationContext
        data?.let {
            Glide.with(context)
                .load(it.avatar_url)
                .apply(
                    RequestOptions().override(
                        context.resources.getDimensionPixelSize(R.dimen.image_user_detail_size),
                        context.resources.getDimensionPixelSize(R.dimen.image_user_detail_size)
                    )
                )
                .centerCrop()
                .into(ivUserImage)

            tvUsername.text = it.login
            tvFollowing.text = it.following.toString()
            tvFollowers.text = it.followers.toString()
            tvRepos.text = it.public_repos.toString()
        }
    }

    companion object {
        const val FETCH_USER_FAILED = "FETCH USER FAILED"
    }
}