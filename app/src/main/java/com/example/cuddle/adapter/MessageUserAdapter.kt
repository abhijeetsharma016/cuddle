package com.example.cuddle.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.cuddle.databinding.UserItemLayoutBinding
import com.example.cuddle.model.userModel
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.cuddle.activity.MessageActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageUserAdapter(val context: Context, val list: ArrayList<String>, val chatKey : List<String>) : RecyclerView.Adapter<MessageUserAdapter.MessageUserViewHolder>() {

    inner class MessageUserViewHolder(val binding: UserItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageUserViewHolder {
        return MessageUserViewHolder(UserItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: MessageUserViewHolder, position: Int) {

        FirebaseDatabase.getInstance().getReference("users").child(list[position]).addListenerForSingleValueEvent(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    if(snapshot.exists()){
                        val data = snapshot.getValue(userModel::class.java)
                        Glide.with(context).load(data!!.image).into(holder.binding.userImage)
                        holder.binding.userName.text = data.name
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
        holder.itemView.setOnClickListener {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("userId", list[position])
            intent.putExtra("chatId", chatKey[position])
            context.startActivity(intent)
        }
        // Bind the data to the view holder
    }

    override fun getItemCount(): Int {
        // Return the size of the user list
        return list.size
    }
}