package com.dicoding.android.fundamental.githubuser.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.android.fundamental.githubuser.R
import com.dicoding.android.fundamental.githubuser.data.ThemePreferences
import com.dicoding.android.fundamental.githubuser.databinding.ActivityUserDetailBinding
import com.dicoding.android.fundamental.githubuser.databinding.FragmentUserBinding
import com.dicoding.android.fundamental.githubuser.ui.adapter.UserSectionPagerAdapter
import com.dicoding.android.fundamental.githubuser.ui.viewmodel.MainViewModel
import com.dicoding.android.fundamental.githubuser.ui.viewmodel.ThemeViewModel
import com.dicoding.android.fundamental.githubuser.ui.viewmodel.UserDetailViewModel
import com.dicoding.android.fundamental.githubuser.ui.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {
    private lateinit var detailBinding: ActivityUserDetailBinding
    private lateinit var fragmentUserBinding: FragmentUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory : ViewModelFactory = ViewModelFactory.getInstance(this)
        val userDetailViewModel : UserDetailViewModel by viewModels { factory }

        fragmentUserBinding = FragmentUserBinding.inflate(layoutInflater)
        detailBinding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        supportActionBar?.hide()

        val username = intent.getStringExtra(EXTRA_USERNAME) as String
        val profileImage = intent.getStringExtra(EXTRA_IMAGE) as String
        val profileUrl = intent.getStringExtra(EXTRA_PROFILE_URL) as String

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
        userSectionPagerAdapter.username = username
        val viewPager2 : ViewPager2 = detailBinding.viewPager
        viewPager2.adapter = userSectionPagerAdapter

        val tabs: TabLayout = detailBinding.tabs

        TabLayoutMediator(tabs, viewPager2) {
            tab, position -> tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val shareButton = detailBinding.shareButton
        val favoriteButton = detailBinding.favoriteButton

        shareButton.setOnClickListener {
            val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(profileUrl))
            startActivity(browseIntent)
        }

        userDetailViewModel.getFavUserByUname(username).observe(
            this, {
                if (it != null) {
                    detailBinding.favoriteButton.apply {
                        this.text = "Favorited"
                        this.setTextColor(resources.getColorStateList(R.color.green))
                        this.background = resources.getDrawable(R.drawable.bg_favorite_outlined_rounded_button)
                        this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_24, 0, 0, 0)
                    }
                    favoriteButton.setOnClickListener {
                        userDetailViewModel.deleteFavoriteUser(username)
                    }
                } else {
                    detailBinding.favoriteButton.apply {
                        this.text = "Add to Favorite"
                        this.setTextColor(resources.getColorStateList(R.color.white))
                        this.background = resources.getDrawable(R.drawable.bg_favorite_solid_rounded_button)
                        this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_favorite_24, 0, 0, 0)
                    }
                    favoriteButton.setOnClickListener {
                        userDetailViewModel.insertFavoriteUser(username, profileImage, profileUrl)
                    }
                }
            }
        )

        val preferences = ThemePreferences.getInstance(dataStore)
        val themeViewModel = ViewModelProvider(this).get(ThemeViewModel::class.java)
        themeViewModel.setPreferences(preferences)

        themeViewModel.getThemeSetting()?.observe(
            this, {
                if (it) {
                    detailBinding.tabs.setBackgroundResource(R.drawable.rounded_user_follow_dark)
                    detailBinding.tabs.tabTextColors = resources.getColorStateList(R.color.white, theme)
                    detailBinding.viewPager.setBackgroundColor(resources.getColor(R.color.black))
                } else {
                    detailBinding.tabs.setBackgroundResource(R.drawable.rounded_user_follow_light)
                    detailBinding.tabs.tabTextColors = resources.getColorStateList(R.color.dark_smoke, theme)
                    detailBinding.viewPager.setBackgroundColor(resources.getColor(R.color.white))

                }
            }
        )

    }

    private fun showLoading(isLoading: Boolean) {
        detailBinding.pbDetailuser.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        val EXTRA_PROFILE_URL = "extra_profile_url"
        val EXTRA_USERNAME = "extra_user"
        val EXTRA_IMAGE = "extra_image"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }

}


