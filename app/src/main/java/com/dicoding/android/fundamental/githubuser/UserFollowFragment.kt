package com.dicoding.android.fundamental.githubuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.android.fundamental.githubuser.databinding.FragmentUserBinding

class UserFollowFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var viewModel: UserDetailViewModel

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

        viewModel = ViewModelProvider(requireActivity()).get(UserDetailViewModel::class.java)

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }

        if (position == 1) {
            viewModel.getFollowers(username)
            viewModel.userFollowers.observe(viewLifecycleOwner) {
                if (it.size == 0 ) {
                    binding.status.text = "No followers"
                    binding.status.visibility = View.VISIBLE
                } else {
                    binding.status.visibility = View.GONE
                    setUserFollow(it)
                }
            }
        } else {
            viewModel.getFollowing(username)
            viewModel.userFollowing.observe(viewLifecycleOwner) {
                if (it.size == 0) {
                    binding.status.text = "No following"
                    binding.status.visibility = View.VISIBLE
                } else {
                    binding.status.visibility = View.GONE
                    setUserFollow(it)
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