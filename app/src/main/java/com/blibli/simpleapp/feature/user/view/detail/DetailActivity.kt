package com.blibli.simpleapp.feature.user.view.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.blibli.simpleapp.R
import com.blibli.simpleapp.SimpleApp
import com.blibli.simpleapp.core.util.ImageHelper
import com.blibli.simpleapp.core.util.ResourcesHelper
import com.blibli.simpleapp.feature.user.model.User
import com.blibli.simpleapp.feature.user.model.enums.ApiCall
import com.blibli.simpleapp.feature.user.presenter.detail.DetailPresenterContract
import com.blibli.simpleapp.feature.user.presenter.detail.DetailPresenterImpl
import com.blibli.simpleapp.feature.user.view.user.UserFragment
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textview.MaterialTextView
import javax.inject.Inject


class DetailActivity : AppCompatActivity(), DetailViewContract {

    @Inject
    lateinit var presenter: DetailPresenterImpl

    private lateinit var tvUsername: MaterialTextView
    private lateinit var tvFollowing: MaterialTextView
    private lateinit var tvFollowers: MaterialTextView
    private lateinit var tvRepos: MaterialTextView
    private lateinit var ivUserImage: ShapeableImageView

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private var username = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        (application as SimpleApp).getUserComponent().inject(this)
//        setPresenter(DetailPresenterImpl(this))

        initView()
        getUsernameIntent()
        setupViewPager()
        setupTabs()
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
            tab.text = if (position == 0) {
                ResourcesHelper.getString(this, R.string.following_label)
            } else {
                ResourcesHelper.getString(this, R.string.followers_label)
            }
        }.attach()
    }

    private fun setupViewPager() {
        viewPager.adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount(): Int = 2

            override fun createFragment(position: Int): Fragment {
                return if (position == 0) {
                    UserFragment.newInstance(1, ApiCall.FETCH_FOLLOWING_DATA.ordinal, username)
                } else {
                    UserFragment.newInstance(1, ApiCall.FETCH_FOLLOWERS_DATA.ordinal, username)
                }
            }
        }
    }

    private fun getUsernameIntent() {
        val usernameFromIntent = intent.getStringExtra(UserFragment.ARG_USER_NAME)
        usernameFromIntent?.let {
            username = it
            presenter.fetchData(it)
        }
    }

    fun getStartIntent(context: Context?): Intent? {
        return Intent(context, DetailActivity::class.java)
    }

    fun putDataToUI(data: User) {
        val context = applicationContext
        data.let {
            ImageHelper.resizeAndBuildImage(
                context,
                it.avatar_url,
                ivUserImage,
                R.dimen.image_user_detail_size
            )

            tvUsername.text = it.login
            tvFollowing.text = it.following.toString()
            tvFollowers.text = it.followers.toString()
            tvRepos.text = it.public_repos.toString()
        }
    }

//    override fun setPresenter(presenter: DetailPresenterContract) {
//        this.presenter = presenter
//    }
}