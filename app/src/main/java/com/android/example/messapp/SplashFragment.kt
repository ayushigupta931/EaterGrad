package com.android.example.messapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val auth: FirebaseAuth = Firebase.auth
        val currentUser = auth.currentUser

        if (currentUser == null)
            Handler(Looper.getMainLooper()).postDelayed(
                { findNavController().navigate(R.id.action_splashFragment_to_loginFragment) },
                1500) // start login activity
        else
            Handler(Looper.getMainLooper()).postDelayed(
                { findNavController().navigate(R.id.action_splashFragment_to_homeFragment2) },
                1500)  // start home activity

    }

}