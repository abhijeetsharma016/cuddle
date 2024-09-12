package com.example.cuddle.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cuddle.MainActivity
import com.example.cuddle.R
import com.example.cuddle.databinding.ActivityRegisterBinding
import com.example.cuddle.model.userModel
import com.example.cuddle.utils.config
import com.example.cuddle.utils.config.hideDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var imageUri: Uri? = null
    private val selectImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            binding.userImage.setImageURI(uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userImage.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.savaData.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        if (binding.userName.text.toString().isEmpty() || binding.userEmail.text.toString()
                .isEmpty() || binding.userLocation.text.toString().isEmpty() || imageUri == null
        ) {
            Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
        } else if (!binding.termsCondition.isChecked) {
            Toast.makeText(this, "Please accept the terms and conditions", Toast.LENGTH_SHORT)
                .show()
        } else {
            uploadImage()
        }
    }

    private fun uploadImage() {
        config.showDialog(this)

        if (imageUri == null) {
            hideDialog()
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            hideDialog()
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        val storageRef = FirebaseStorage.getInstance().getReference("profile")
            .child(user.uid).child("profile.jpg")

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    storeData(it)
                }.addOnFailureListener {
                    hideDialog()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                hideDialog()
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(imageUrl: Uri?) {
        // Fetch phone number from the Intent
        val phoneNumber = intent.getStringExtra("phoneNumber")

        if (phoneNumber == null) {
            hideDialog()
            Toast.makeText(this, "Phone number not found", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a userModel object
        val data = userModel(
            number = phoneNumber,  // Assign the fetched phone number here
            name = binding.userName.text.toString(),
            email = binding.userEmail.text.toString(),
            city = binding.userLocation.text.toString(),
            gender = "",
            relationShipStatus = "",
            star = "",
            image = imageUrl.toString(),
            age = "",
            status = ""
        )

        // Store the data in Firebase under the user's phone number
        FirebaseDatabase.getInstance().getReference("users")
            .child(phoneNumber)  // Save data under the phone number
            .setValue(data).addOnCompleteListener {
                hideDialog()
                if (it.isSuccessful) {
                    Toast.makeText(this, "Data inserted", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}