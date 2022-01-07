package com.android.example.messapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        var intent: Intent
        val auth: FirebaseAuth = Firebase.auth
        val currentUser = auth.currentUser

        if (currentUser == null)
            intent = Intent(this@SplashActivity, LoginActivity::class.java)    // start login activity
        else
            intent = Intent(this@SplashActivity, MainActivity::class.java)    // start home activity

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        Handler(Looper.getMainLooper()).postDelayed({ startActivity(intent) }, 2000)
    }
}