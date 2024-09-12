package com.example.cuddle.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cuddle.MainActivity
import com.example.cuddle.R
import com.example.cuddle.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class loginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    val auth = FirebaseAuth.getInstance()
    private var storedVerificationId: String? = null
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog =
            AlertDialog.Builder(this).setView(R.layout.loading_layout).setCancelable(false).create()

        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.sendOtpButton.setOnClickListener {
            if (binding.userNumber.text!!.isEmpty()) {
                binding.userNumber.error = "Please enter your number"
            } else {
                sendOtp(binding.userNumber.text.toString())
            }
        }

        binding.verifyOtpButton.setOnClickListener {
            if (binding.userOTP.text!!.isEmpty()) {
                binding.userOTP.error = "Please enter your OTP"
            } else {
                verifyOtp(binding.userOTP.text.toString())
            }
        }
    }

    private fun verifyOtp(otp: String) {
        if (storedVerificationId == null) {
            Toast.makeText(this, "Verification ID is null", Toast.LENGTH_SHORT).show()
            return
        }
        dialog.show()
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, otp)
        signInWithPhoneAuthCredential(credential)
    }

    private fun sendOtp(number: String) {
        dialog.show()
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                dialog.dismiss()
                Toast.makeText(this@loginActivity, "Verification failed", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                storedVerificationId = verificationId
                dialog.dismiss()
                binding.numberLayout.visibility = View.GONE
                binding.otpLayout.visibility = View.VISIBLE
                Toast.makeText(this@loginActivity, "OTP sent successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$number") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    checkUserExistence(binding.userNumber.text.toString())
                } else {
                    dialog.dismiss()
                    Toast.makeText(this, task.exception?.message!!, Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun checkUserExistence(number: String) {
        // Since the numbers in your database are stored with "+91" prefix, include it here
        val phoneNumberWithCountryCode = "+91$number"

        // Check the existence in the database
        FirebaseDatabase.getInstance().getReference("users").child(phoneNumberWithCountryCode)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    dialog.dismiss()
                    if (snapshot.exists()) {
                        startActivity(Intent(this@loginActivity, MainActivity::class.java))
                    } else {
                        val intent = Intent(this@loginActivity, RegisterActivity::class.java)
                        intent.putExtra("phoneNumber", phoneNumberWithCountryCode)
                        startActivity(intent)
                    }
                    finish()
                }


                override fun onCancelled(error: DatabaseError) {
                    dialog.dismiss()
                    Toast.makeText(this@loginActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}