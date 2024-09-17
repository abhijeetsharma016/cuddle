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
import com.example.cuddle.fragments.DatingFragment.Companion
import com.example.cuddle.fragments.DatingFragment.Companion.list
import com.example.cuddle.utils.config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class MessageFragment : Fragment() {
    private lateinit var binding: FragmentMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(layoutInflater)

        getData()
        return binding.root
    }
    private fun getData() {
        if (!isAdded) return // Ensure fragment is attached

        config.showDialog(requireContext()) // Only call this if fragment is attached

        val list = arrayListOf<String>()
        val newList = arrayListOf<String>()
        val currentId = FirebaseAuth.getInstance().currentUser?.phoneNumber

        if (currentId == null) {
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseDatabase.getInstance().getReference("chats")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!isAdded) return // Check if fragment is still attached

                    for (data in snapshot.children) {
                        if (data.key!!.contains(currentId)) {
                            list.add(data.key!!.replace(currentId, ""))
                            newList.add(data.key!!)
                        }
                    }

                    binding.recyclerView.adapter = MessageUserAdapter(requireContext(), list, newList)
                    config.hideDialog() // Hide dialog only if fragment is attached
                }

                override fun onCancelled(error: DatabaseError) {
                    if (!isAdded) return // Check if fragment is still attached
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }


}