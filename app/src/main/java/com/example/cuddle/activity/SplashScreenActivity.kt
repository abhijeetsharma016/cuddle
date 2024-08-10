package com.example.cuddle.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cuddle.MainActivity
import com.example.cuddle.R
import com.example.cuddle.auth.loginActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val user = FirebaseAuth.getInstance().currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            if(user == null){
                startActivity(Intent(this, loginActivity::class.java))
                finish()
                }
            else{
                startActivity(Intent(this, loginActivity::class.java))
                finish()
            }
        },2000)
    }
}