package com.dicoding.android.fundamental.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.android.fundamental.githubuser.data.response.User
import com.dicoding.android.fundamental.githubuser.databinding.ActivityFavoriteUserBinding
import com.dicoding.android.fundamental.githubuser.ui.adapter.UserAdapter
import com.dicoding.android.fundamental.githubuser.ui.viewmodel.FavoriteUserViewModel
import com.dicoding.android.fundamental.githubuser.ui.viewmodel.MainViewModel
import com.dicoding.android.fundamental.githubuser.ui.viewmodel.ViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var favoriteUserBinding: ActivityFavoriteUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        favoriteUserBinding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(favoriteUserBinding.root)

        supportActionBar?.title = "My Favorite User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val factory : ViewModelFactory = ViewModelFactory.getInstance(this)
        val favoriteUserViewModel : FavoriteUserViewModel by viewModels { factory }

        val layoutManager = LinearLayoutManager(this)
        favoriteUserBinding.rvFavUsers.layoutManager = layoutManager

        favoriteUserViewModel.getFavoriteUser().observe(
            this, {
                val users = mutableListOf<User>()

                for ( favUser in it ) {
                    val user = User(
                        favUser.username,
                        favUser.avatarUrl.toString(),
                        favUser.profileUrl
                    )
                    users.add(user)
                }

                setUser(users.toList())
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUser(users: List<User>) {
        val adapter = UserAdapter(users)
        favoriteUserBinding.rvFavUsers.adapter = adapter
        favoriteUserBinding.rvFavUsers.setHasFixedSize(true)
    }
}