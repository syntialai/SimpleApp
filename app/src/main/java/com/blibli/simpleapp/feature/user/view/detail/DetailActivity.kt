package com.blibli.simpleapp.feature.user.view.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.blibli.simpleapp.R
import com.blibli.simpleapp.core.util.ImageHelper
import com.blibli.simpleapp.core.util.ResourcesHelper
import com.blibli.simpleapp.feature.user.model.enums.ApiCall
import com.blibli.simpleapp.feature.user.view.user.UserFragment
import com.blibli.simpleapp.feature.user.viewmodel.DetailViewModel
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textview.MaterialTextView
import dagger.android.AndroidInjection
import javax.inject.Inject

class DetailActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: DetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)
    }

    private lateinit var tvUsername: MaterialTextView
    private lateinit var tvFollowing: MaterialTextView
    private lateinit var tvFollowers: MaterialTextView
    private lateinit var tvRepos: MaterialTextView
    private lateinit var ivUserImage: ShapeableImageView
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        getUsernameIntent()
        initVar()

        viewModel.username.observe(this, {
            setupViewPager(it)
        })

        viewModel.isLoadingData.observe(this, {
            viewModel.data.value?.let { data -> putDataToUI(data) }
        })
    }

    private fun initVar() {
        tvUsername = findViewById(R.id.tv_user_detail_username)
        tvFollowing = findViewById(R.id.tv_user_detail_following)
        tvFollowers = findViewById(R.id.tv_user_detail_followers)
        tvRepos = findViewById(R.id.tv_user_detail_repos)
        ivUserImage = findViewById(R.id.iv_user_detail_image)
        tabLayout = findViewById(R.id.tab_users)
        viewPager = findViewById(R.id.viewpager_user)

        setupViewPager(viewModel.username.value!!)
        setupTabs()
    }

    private fun putDataToUI(data: com.blibli.simpleapp.feature.user.db.model.User) {
        val context = applicationContext
        data.let { user ->
            user.avatar_url?.let { image ->
                ImageHelper.resizeAndBuildImage(
                    context,
                    image,
                    ivUserImage,
                    R.dimen.image_user_detail_size
                )
            }

            tvUsername.text = user.login
            tvFollowing.text = user.following.toString()
            tvFollowers.text = user.followers.toString()
            tvRepos.text = user.public_repos.toString()
        }
    }

    private fun setupTabs() {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) {
                ResourcesHelper.getString(this, R.string.following_label)
            } else {
                ResourcesHelper.getString(this, R.string.followers_label)
            }
        }.attach()
    }

    private fun setupViewPager(username: String) {
        viewPager.adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount(): Int = 2

            override fun createFragment(position: Int): Fragment {
                return if (position == 0) {
                    UserFragment.newInstance(
                        1, ApiCall.FETCH_FOLLOWING_DATA.ordinal, username
                    )
                } else {
                    UserFragment.newInstance(
                        1, ApiCall.FETCH_FOLLOWERS_DATA.ordinal, username
                    )
                }
            }
        }
    }

    private fun getUsernameIntent() {
        val usernameFromIntent = intent.getStringExtra(UserFragment.ARG_USER_NAME)
        usernameFromIntent?.let {
            viewModel.fetchData(it)
        }
    }

    fun getStartIntent(context: Context?): Intent? {
        return Intent(context, DetailActivity::class.java)
    }
}
