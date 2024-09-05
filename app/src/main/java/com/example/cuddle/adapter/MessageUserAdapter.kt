package com.example.cuddle.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.cuddle.databinding.UserItemLayoutBinding
import com.example.cuddle.model.userModel
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide

class MessageUserAdapter(val context: Context, val userList: ArrayList<userModel>) : RecyclerView.Adapter<MessageUserAdapter.MessageUserViewHolder>() {

    inner class MessageUserViewHolder(val binding: UserItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageUserViewHolder {
        // Inflate the layout and return the view holder
        return MessageUserViewHolder(UserItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: MessageUserViewHolder, position: Int) {
        Glide.with(context).load(userList[position].email).into(holder.binding.userImage)

        holder.binding.userName.text = userList[position].name
        // Bind the data to the view holder
    }

    override fun getItemCount(): Int {
        // Return the size of the user list
        return userList.size
    }
}