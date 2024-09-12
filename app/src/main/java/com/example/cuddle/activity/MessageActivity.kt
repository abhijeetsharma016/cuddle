package com.example.cuddle.activity

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.cuddle.databinding.ActivityMessageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date

class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
         binding.sendButton.setOnClickListener {
             if(binding.yourMessage.text!!.isEmpty()){
                 Toast.makeText(this, "Please enter your message", Toast.LENGTH_SHORT).show()
             }else{
                 sendMessage(binding.yourMessage.text.toString())
             }
        }
    }

    private fun sendMessage(msg: String) {
        val receiverId = intent.getStringExtra("userId")
        val senderId = FirebaseAuth.getInstance().currentUser!!.phoneNumber

        val chatId = senderId + receiverId
        val currentDate = SimpleDateFormat("ddMMyyyy").format(Date())
        val currentTime = SimpleDateFormat("HHmmss").format(Date())

        val map = hashMapOf<String, String>()
        map["message"] = msg
        map["senderId"] = senderId!!
        map["date"] = currentDate
        map["time"] = currentTime

        val reference = FirebaseDatabase.getInstance().getReference().child("chats").child(chatId)

            reference.child(reference.push().key!!).setValue(map).addOnCompleteListener{
                if(it.isSuccessful){
                    binding.yourMessage.text = null
                    Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
    }
}