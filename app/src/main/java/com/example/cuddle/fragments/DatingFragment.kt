package com.example.cuddle.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.cuddle.R
import com.example.cuddle.adapter.DatingAdapter
import com.example.cuddle.databinding.FragmentDatingBinding
import com.example.cuddle.model.userModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction

class DatingFragment : Fragment() {

    private lateinit var binding: FragmentDatingBinding
    private lateinit var manager: CardStackLayoutManager
    private var currentId: String? = null  // To store the current user's ID


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDatingBinding.inflate(inflater, container, false)

        currentId = FirebaseAuth.getInstance().currentUser?.phoneNumber

        getData()

        return binding.root
    }

    private fun init() {
        manager = CardStackLayoutManager(requireContext(), object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
                // Handle card dragging event if needed
            }

            override fun onCardSwiped(direction: Direction?) {
                // Handle card swiped event if needed
                if (manager.topPosition == list!!.size) {
                    Toast.makeText(requireContext(), "this is last card", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCardRewound() {
                // Handle card rewind event if needed
            }

            override fun onCardCanceled() {
                // Handle card canceled event if needed
            }

            override fun onCardAppeared(view: View?, position: Int) {
                // Handle card appeared event if needed
            }

            override fun onCardDisappeared(view: View?, position: Int) {
                // Handle card disappeared event if needed
            }
        })

        manager.setVisibleCount(3)
        manager.setTranslationInterval(0.6f)
        manager.setScaleInterval(0.8f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)


        // Attach the CardStackLayoutManager to the CardStackView
        binding.cardStackView.layoutManager = manager
    }

    companion object{
         var list: ArrayList<userModel>? = null

    }
    private fun getData() {
        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("DatingFragment", "onDataChange: ${snapshot.toString()}")
                if (snapshot.exists()) {
                    list = arrayListOf()
                    for (data in snapshot.children) {
                        val model = data.getValue(userModel::class.java)
                        model?.let {
                            if (model.number != currentId) {  // Assuming userModel has a 'uid' field
                                list!!.add(it)
                            }
                        }
                    }

                    list!!.shuffle()
                    init()

                    binding.cardStackView.layoutManager = manager
                    binding.cardStackView.itemAnimator = DefaultItemAnimator()

                    binding.cardStackView.adapter = DatingAdapter(requireContext(), list!!)
                } else {
                    Toast.makeText(requireContext(), "No Data Found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
