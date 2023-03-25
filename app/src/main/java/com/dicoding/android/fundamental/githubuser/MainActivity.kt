package com.dicoding.android.fundamental.githubuser

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.android.fundamental.githubuser.databinding.ActivityMainBinding
import com.dicoding.android.fundamental.githubuser.databinding.ItemUserBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var itemBinding: ItemUserBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        itemBinding = ItemUserBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val layoutManager = LinearLayoutManager(this)
        mainBinding.rvUsers.layoutManager = layoutManager

        supportActionBar?.title = resources.getString(R.string.app_name)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = mainBinding.svUsers

        mainViewModel.users.observe(
            this, {
                setUser(it)
            }
        )

        mainViewModel.isLoading.observe(
            this, {
                showLoading(it)
            }
        )

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setIconifiedByDefault(false)
        searchView.focusable = View.NOT_FOCUSABLE
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    mainViewModel.getUsers(query)
                    searchView.clearFocus()
                    return true
                }
            }
        )
        val clearButton = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        clearButton.setOnClickListener {
            searchView.setQuery("", false)
            mainViewModel.getUsers("jay")
        }
    }

    private fun showLoading(isLoading: Boolean) {
        mainBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
     }

    private fun setUser(users: List<User>) {
        val adapter = UserAdapter(users)
        mainBinding.rvUsers.adapter = adapter
        mainBinding.rvUsers.setHasFixedSize(true)
    }
}