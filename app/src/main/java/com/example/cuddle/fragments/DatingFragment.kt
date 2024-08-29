package com.example.cuddle.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cuddle.R
import com.example.cuddle.databinding.ActivityLoginBinding
import com.example.cuddle.databinding.FragmentDatingBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class DatingFragment : Fragment() {

    private lateinit var binding: FragmentDatingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDatingBinding.inflate(layoutInflater)

        getData()

        return binding.root
    }

    private fun getData(){
        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("DatingFragment", "onDataChange: ${snapshot.toString()}")

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "error: ${error.message}", Toast.LENGTH_SHORT).show()

            }

        })
    }

}