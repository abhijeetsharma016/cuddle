package com.example.cuddle.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cuddle.R
import com.example.cuddle.adapter.MessageUserAdapter
import com.example.cuddle.databinding.FragmentMessageBinding
import com.example.cuddle.utils.config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageFragment : Fragment() {
    private lateinit var binding: FragmentMessageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageBinding.inflate(inflater, container, false)

        if (isAdded) { // Ensure fragment is attached
            getData()
        }

        return binding.root
    }

    private fun getData() {
        if (!isAdded) return

        val context = requireContext()
        config.showDialog(context)

        val userList = arrayListOf<String>()
        val chatSet = mutableSetOf<String>()
        val currentId = FirebaseAuth.getInstance().currentUser?.phoneNumber

        if (currentId == null) {
            Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseDatabase.getInstance().getReference("chats")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!isAdded) return

                    chatSet.clear()

                    for (data in snapshot.children) {
                        val chatKey = data.key ?: continue
                        val senderId = chatKey.substring(0, currentId.length)
                        val receiverId = chatKey.substring(currentId.length)

                        // Check if the current user is either the sender or the receiver
                        if (senderId == currentId || receiverId == currentId) {
                            val otherUserId = if (senderId == currentId) receiverId else senderId
                            val bidirectionalKey1 = currentId + otherUserId
                            val bidirectionalKey2 = otherUserId + currentId

                            if (!chatSet.contains(bidirectionalKey1) && !chatSet.contains(bidirectionalKey2)) {
                                chatSet.add(chatKey)
                                userList.add(otherUserId)
                            }
                        }
                    }

                    binding.recyclerView.adapter = MessageUserAdapter(context, userList, chatSet.toList())
                    config.hideDialog()
                }

                override fun onCancelled(error: DatabaseError) {
                    if (!isAdded) return
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

}
