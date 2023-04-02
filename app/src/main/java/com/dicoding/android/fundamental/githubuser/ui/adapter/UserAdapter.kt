package com.dicoding.android.fundamental.githubuser.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.android.fundamental.githubuser.data.response.User
import com.dicoding.android.fundamental.githubuser.databinding.ItemUserBinding
import com.dicoding.android.fundamental.githubuser.ui.UserDetailActivity

class UserAdapter(private val listUser : List<User>) : RecyclerView.Adapter<UserAdapter.ListUserViewHolder>() {

    inner class ListUserViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserViewHolder {
        val layout = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListUserViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ListUserViewHolder, position: Int) {
        val username = listUser[position].login
        val imageURL = listUser[position].avatarUrl
        holder.binding.tvUsername.text = username
        Glide.with(holder.itemView.context)
            .load(imageURL)
            .circleCrop()
            .into(holder.binding.ivUserImage)
        holder.binding.itemUser.setOnClickListener {
            val detailIntent = Intent(holder.itemView.context, UserDetailActivity::class.java).apply {
                putExtra(UserDetailActivity.EXTRA_USERNAME, username)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            holder.itemView.context.startActivity(detailIntent)
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }
}