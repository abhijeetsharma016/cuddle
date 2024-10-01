package com.example.cuddle.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cuddle.activity.MessageActivity
import com.example.cuddle.databinding.ItemUserLayoutBinding
import com.example.cuddle.model.userModel
import com.google.firebase.auth.FirebaseAuth

class DatingAdapter(val context: Context, val list: ArrayList<userModel>, val chatKey : List<String>) : RecyclerView.Adapter<DatingAdapter.DatingViewHolder>() {
    inner class DatingViewHolder(val binding: ItemUserLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingViewHolder {
        return DatingViewHolder(ItemUserLayoutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: DatingViewHolder, position: Int) {
        holder.binding.textView4.text = list[position].name
        holder.binding.textView3.text = list[position].email

        Glide.with(context).load(list[position].image).into(holder.binding.userImage)

        // Find the correct chatKey for this user
        val otherUserId = list[position].number
        val currentId = FirebaseAuth.getInstance().currentUser?.phoneNumber

        // Find the corresponding chatKey
        val chatId = chatKey.find {
            it.contains(currentId!!) && it.contains(otherUserId!!)
        }

        holder.binding.chat.setOnClickListener {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("userId", otherUserId)
            intent.putExtra("chatId", chatId) // Pass chatId to MessageActivity
            context.startActivity(intent)
        }
    }

}