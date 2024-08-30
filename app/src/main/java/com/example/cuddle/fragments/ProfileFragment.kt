package com.example.cuddle.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.cuddle.R
import com.example.cuddle.databinding.FragmentDatingBinding
import com.example.cuddle.databinding.FragmentProfileBinding
import com.example.cuddle.model.userModel
import com.example.cuddle.utils.config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        config.showDialog(requireContext())

        binding = FragmentProfileBinding.inflate(layoutInflater)
        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!).get()
            .addOnSuccessListener {
                if(it.exists()){
                    val data = it.getValue(userModel::class.java)
                    binding.inputName.setText( data!!.name.toString())
                    binding.inputEmail.setText( data!!.email.toString())
                    binding.inputCity.setText( data!!.city.toString())
                    binding.inputNumber.setText( data!!.number.toString())


                    Glide.with(requireContext()).load(data.image).placeholder(R.drawable.user).into(binding.userImage)

                    config.hideDialog()
                }
            }
        return binding.root
    }

}