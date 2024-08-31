package com.example.cuddle.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.cuddle.databinding.ItemUserLayoutBinding
import com.example.cuddle.model.userModel

class DatingAdapter(val context: Context, val list: ArrayList<userModel>) : RecyclerView.Adapter<DatingAdapter.DatingViewHolder>() {
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
    }
}