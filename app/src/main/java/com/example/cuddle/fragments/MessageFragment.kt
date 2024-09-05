package com.example.cuddle.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cuddle.R
import com.example.cuddle.adapter.MessageUserAdapter
import com.example.cuddle.databinding.FragmentMessageBinding
import com.example.cuddle.fragments.DatingFragment.Companion.list

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
        binding.recyclerView.adapter = MessageUserAdapter(requireContext(), list!!)
        return binding.root
    }

}