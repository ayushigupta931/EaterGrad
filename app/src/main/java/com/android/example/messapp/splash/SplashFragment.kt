package com.android.example.messapp.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.example.messapp.R
import com.android.example.messapp.login.UserViewmodel
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
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        val auth: FirebaseAuth = Firebase.auth
        val currentUser = auth.currentUser
        val userViewmodel by viewModels<UserViewmodel>()


        if (currentUser == null)
            Handler(Looper.getMainLooper()).postDelayed(
                { findNavController().navigate(R.id.action_splashFragment_to_loginFragment) },
                500
            )
        else {
            lifecycleScope.launchWhenCreated {
                val role = auth.currentUser?.email?.let { userViewmodel.checkRole(it) }
                if (role == 1)
                    Handler(Looper.getMainLooper()).postDelayed(
                        { findNavController().navigate(R.id.action_splashFragment_to_adminFragment) },
                        500
                    )
                else
                    Handler(Looper.getMainLooper()).postDelayed(
                        { findNavController().navigate(R.id.action_splashFragment_to_homeFragment2) },
                        500
                    )
            }
        }
    }

}