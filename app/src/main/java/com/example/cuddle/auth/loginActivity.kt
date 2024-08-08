package com.example.cuddle.auth

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cuddle.MainActivity
import com.example.cuddle.R
import com.example.cuddle.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class loginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    val auth = FirebaseAuth.getInstance()
    private var storedVerificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendOtpButton.setOnClickListener {
            if(binding.userNumber.text!!.isEmpty()){
                binding.userNumber.error = "Please enter your number"
            }
            else{
                sendOtp(binding.userNumber.text.toString())
            }
        }

        binding.verifyOtpButton.setOnClickListener{
            if(binding.userOTP.text!!.isEmpty()){
                binding.userOTP.error = "Please enter your OTP"
            }
            else{
                verifyOtp(binding.userOTP.text.toString())
            }
        }
    }

    private fun verifyOtp(otp: String) {
        if (storedVerificationId == null) {
            Toast.makeText(this, "Verification ID is null", Toast.LENGTH_SHORT).show()
            return
        }

        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, otp)
        signInWithPhoneAuthCredential(credential)
    }

    private fun sendOtp(number: String) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@loginActivity, "Verification failed", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                storedVerificationId = verificationId
                binding.numberLayout.visibility = GONE
                binding.otpLayout.visibility = VISIBLE
                Toast.makeText(this@loginActivity, "OTP sent successfully", Toast.LENGTH_SHORT).show()
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
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, task.exception?.message!!, Toast.LENGTH_SHORT).show()
                }
            }
    }
}