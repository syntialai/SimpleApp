package com.blibli.simpleapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.blibli.futurekotlin.builder.RetrofitClient
import com.blibli.simpleapp.data.User
import com.blibli.simpleapp.service.UserService
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textview.MaterialTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var tvUsername: MaterialTextView
    private lateinit var tvFollowing: MaterialTextView
    private lateinit var tvFollowers: MaterialTextView
    private lateinit var tvRepos: MaterialTextView
    private lateinit var ivUserImage: ShapeableImageView

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private lateinit var service: UserService
    private var username = ""
    private var data: User? = null
    private var following: ArrayList<User> = ArrayList()
    private var followers: ArrayList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        service = RetrofitClient.createService()

        initView()
        getUsernameIntent()
        fetchData()
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
            tab.text = when(position) {
                0 -> resources.getString(R.string.following_label)
                else -> resources.getString(R.string.followers_label)
            }
        }.attach()
    }

    private fun setupViewPager() {
        viewPager.adapter = object: FragmentStateAdapter(this) {

            override fun getItemCount(): Int = 2

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> UserFragment.newInstance(1, following)
                    else -> UserFragment.newInstance(1, followers)
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
        service
            .getUserByUsername(username)
            .enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    if (response.isSuccessful) {
                        data = response.body() as User

                        fetchFollowingData()
                        fetchFollowersData()
                        putDataToUI()
                    } else {
                        Log.d(FETCH_USER_FAILED, response.toString())
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d(FETCH_USER_FAILED, t.toString())
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

    private fun fetchFollowingData() {
        service.getUserByUsernameAndCategory(username, "following")
            .enqueue(object: Callback<ArrayList<User>> {
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    if (response.isSuccessful) {
                        following = response.body() as ArrayList<User>
                    } else {
                        Log.d(FETCH_FOLLOWING_FAILED, response.toString())
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.d(FETCH_FOLLOWING_FAILED, t.toString())
                }
            })
    }

    private fun fetchFollowersData() {
        service.getUserByUsernameAndCategory(username, "followers")
            .enqueue(object: Callback<ArrayList<User>> {
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    if (response.isSuccessful) {
                        followers = response.body() as ArrayList<User>
                        setupViewPager()
                        setupTabs()
                    } else {
                        Log.d(FETCH_FOLLOWERS_FAILED, response.toString())
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.d(FETCH_FOLLOWERS_FAILED, t.toString())
                }
            })
    }

    companion object {
        const val FETCH_USER_FAILED = "FETCH USER FAILED"
        const val FETCH_FOLLOWING_FAILED = "FETCH FOLLOWING_FAILED"
        const val FETCH_FOLLOWERS_FAILED = "FETCH FOLLOWERS_FAILED"
    }
}