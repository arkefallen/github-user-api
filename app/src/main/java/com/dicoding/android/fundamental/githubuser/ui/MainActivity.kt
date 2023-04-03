package com.dicoding.android.fundamental.githubuser.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.android.fundamental.githubuser.R
import com.dicoding.android.fundamental.githubuser.data.ThemePreferences
import com.dicoding.android.fundamental.githubuser.data.response.User
import com.dicoding.android.fundamental.githubuser.databinding.ActivityMainBinding
import com.dicoding.android.fundamental.githubuser.databinding.ActivityThemeBinding
import com.dicoding.android.fundamental.githubuser.databinding.ActivityUserDetailBinding
import com.dicoding.android.fundamental.githubuser.databinding.FragmentUserBinding
import com.dicoding.android.fundamental.githubuser.databinding.ItemUserBinding
import com.dicoding.android.fundamental.githubuser.ui.adapter.UserAdapter
import com.dicoding.android.fundamental.githubuser.ui.viewmodel.MainViewModel
import com.dicoding.android.fundamental.githubuser.ui.viewmodel.ThemeViewModel
import com.dicoding.android.fundamental.githubuser.ui.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory : ViewModelFactory = ViewModelFactory.getInstance(this)
        val mainViewModel : MainViewModel by viewModels { factory }

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val preferences = ThemePreferences.getInstance(dataStore)
        val themeViewModel = ViewModelProvider(this).get(ThemeViewModel::class.java)
        themeViewModel.setPreferences(preferences)

        themeViewModel.getThemeSetting()?.observe(
            this, {
                if (it) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        )



        val layoutManager = LinearLayoutManager(this)
        mainBinding.rvUsers.layoutManager = layoutManager

        supportActionBar?.title = resources.getString(R.string.app_name)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = mainBinding.svUsers

        mainViewModel.setUsers("jay")

        mainViewModel.getListUsers().observe(
            this, {
                if (it != null) {
                    setUser(it)
                }
            }
        )

        mainViewModel.getLoading().observe(
            this, {
                if (it != null) {
                    showLoading(it)
                }
            }
        )

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setIconifiedByDefault(false)
        searchView.focusable = View.NOT_FOCUSABLE
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    mainViewModel.setUsers(query)
                    searchView.clearFocus()
                    return true
                }
            }
        )
        val clearButton = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        clearButton.setOnClickListener {
            searchView.setQuery("", false)
            mainViewModel.setUsers("jay")
        }
    }

    private fun showLoading(isLoading: Boolean) {
        mainBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
     }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.favorite_users -> {
                val favIntent = Intent(this@MainActivity, FavoriteUserActivity::class.java )
                startActivity(favIntent)
                return true
            }
            else -> {
                val themeIntent = Intent(this@MainActivity, ThemeActivity::class.java)
                startActivity(themeIntent)
                return true
            }
        }
    }

    private fun setUser(users: List<User>) {
        val adapter = UserAdapter(users)
        mainBinding.rvUsers.adapter = adapter
        mainBinding.rvUsers.setHasFixedSize(true)
    }
}