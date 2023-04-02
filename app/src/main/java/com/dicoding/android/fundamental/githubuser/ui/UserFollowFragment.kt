package com.dicoding.android.fundamental.githubuser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.android.fundamental.githubuser.data.response.User
import com.dicoding.android.fundamental.githubuser.databinding.FragmentUserBinding
import com.dicoding.android.fundamental.githubuser.ui.adapter.UserAdapter
import com.dicoding.android.fundamental.githubuser.ui.viewmodel.UserDetailViewModel
import com.dicoding.android.fundamental.githubuser.ui.viewmodel.ViewModelFactory

class UserFollowFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding

    companion object {
        val ARG_POSITION = "position"
        val ARG_USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var position = 0
        var username = ""

        val viewModel: UserDetailViewModel = ViewModelProvider(requireActivity()).get()

        viewModel.getLoading().observe(viewLifecycleOwner) {
            showLoading(it)
        }

        binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }

        if (position == 1) {
            viewModel.setUserFollowersByUsername(username)
            viewModel.getUserFollowers().observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.avLottie.visibility = View.GONE
                    binding.status.visibility = View.GONE
                    setUserFollow(it)
                }
                if (it.isEmpty()) {
                    binding.status.text = "No followers"
                    binding.avLottie.visibility = View.VISIBLE
                    binding.status.visibility = View.VISIBLE
                }
            }
        } else {
            viewModel.setUserFollowingByUsername(username)
            viewModel.getUserFollowing().observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.avLottie.visibility = View.GONE
                    binding.status.visibility = View.GONE
                    setUserFollow(it)
                }
                if (it.isEmpty()) {
                    binding.status.text = "No following"
                    binding.avLottie.visibility = View.VISIBLE
                    binding.status.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbUserFollow.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUserFollow(users: List<User>) {
        val adapter = UserAdapter(users)
        binding.rvFollow.adapter = adapter
        binding.rvFollow.setHasFixedSize(true)
    }

}