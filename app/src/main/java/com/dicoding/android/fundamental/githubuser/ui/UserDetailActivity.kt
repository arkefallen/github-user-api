package com.dicoding.android.fundamental.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.android.fundamental.githubuser.R
import com.dicoding.android.fundamental.githubuser.databinding.ActivityUserDetailBinding
import com.dicoding.android.fundamental.githubuser.ui.adapter.UserSectionPagerAdapter
import com.dicoding.android.fundamental.githubuser.ui.viewmodel.MainViewModel
import com.dicoding.android.fundamental.githubuser.ui.viewmodel.UserDetailViewModel
import com.dicoding.android.fundamental.githubuser.ui.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {
    private lateinit var detailBinding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory : ViewModelFactory = ViewModelFactory.getInstance(this)
        val userDetailViewModel : UserDetailViewModel by viewModels { factory }

        detailBinding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        supportActionBar?.hide()

        val username = intent.getStringExtra(EXTRA_USERNAME)

        detailBinding.apply {
            this.tvName.visibility = View.GONE
            this.tvUsernameDetail.visibility = View.GONE
            this.tvFollowers.visibility = View.GONE
            this.tvFollowing.visibility = View.GONE
            this.ivAvatar.visibility = View.GONE
            this.favoriteButton.visibility = View.GONE
            this.shareButton.visibility = View.GONE
        }

        userDetailViewModel.setUserDetail(username)

        userDetailViewModel.getFullname().observe(
            this, {
                if (it == null) {
                    detailBinding.tvName.visibility = View.GONE
                } else {
                    detailBinding.tvName.visibility = View.VISIBLE
                    detailBinding.tvName.text = it
                }
            }
        )


        userDetailViewModel.getUsername().observe(
            this, {
                if (it == null) {
                    detailBinding.tvUsernameDetail.visibility = View.GONE
                } else {
                    detailBinding.apply {
                        this.tvUsernameDetail.visibility = View.VISIBLE
                        this.favoriteButton.visibility = View.VISIBLE
                        this.shareButton.visibility = View.VISIBLE
                        this.tvUsernameDetail.text = it.toString()
                    }
                }
            }
        )

        userDetailViewModel.getProfileURL().observe(
            this, {
                detailBinding.ivAvatar.visibility = View.VISIBLE
                Glide.with(this@UserDetailActivity)
                    .load(it)
                    .circleCrop()
                    .into(detailBinding.ivAvatar)
            }
        )

        userDetailViewModel.getTotalFollowers().observe(
            this, {
                detailBinding.tvFollowers.visibility = View.VISIBLE
                detailBinding.tvFollowers.text = "${it} Followers"
            }
        )

        userDetailViewModel.getTotalFollowing().observe(
            this, {
                detailBinding.tvFollowing.visibility = View.VISIBLE
                detailBinding.tvFollowing.text = "${it} Following"
            }
        )

        userDetailViewModel.getLoading().observe(
            this, {
                showLoading(it)
            }
        )

        val userSectionPagerAdapter = UserSectionPagerAdapter(this)
        userSectionPagerAdapter.username = username.toString()
        val viewPager2 : ViewPager2 = detailBinding.viewPager
        viewPager2.adapter = userSectionPagerAdapter

        val tabs: TabLayout = detailBinding.tabs

        TabLayoutMediator(tabs, viewPager2) {
            tab, position -> tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }

    private fun showLoading(isLoading: Boolean) {
        detailBinding.pbDetailuser.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        val EXTRA_USERNAME = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }

}