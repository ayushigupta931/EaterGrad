package com.android.example.messapp.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.example.messapp.R
import com.android.example.messapp.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val userViewmodel by viewModels<UserViewmodel>()
    private val getActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                binding.progressBar1.visibility = View.VISIBLE
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account?.idToken!!)
                } catch (e: ApiException) {
                    binding.progressBar1.visibility = View.INVISIBLE
                    Toast.makeText(requireActivity(), "Sign In failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        auth = Firebase.auth
        binding.googleSignIn.setOnClickListener {
            signIn()
            binding.progressBar1.visibility = View.VISIBLE
        }
        createRequest()
    }

    private fun createRequest() {
        lifecycleScope.launch {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        }
    }

    private fun signIn() {
        lifecycleScope.launch {
            val signInIntent = googleSignInClient.signInIntent
            getActivityResult.launch(signInIntent)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        lifecycleScope.launch {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        binding.progressBar1.visibility = View.INVISIBLE
                        Toast.makeText(
                            requireActivity(),
                            "Signed in successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        lifecycleScope.launchWhenCreated {
                            val role = auth.currentUser?.email?.let { userViewmodel.checkRole(it) }
                            if (role == 1)
                                findNavController().navigate(R.id.action_loginFragment_to_adminFragment)
                            else {
                                if (userViewmodel.isNewUser(auth.uid)) {
                                    userViewmodel.createNewUser(
                                        auth.uid, auth.currentUser?.displayName,
                                        auth.currentUser?.email
                                    )
                                }
                                findNavController().navigate(R.id.action_loginFragment_to_homeFragment2)
                            }
                        }


                    } else {
                        binding.progressBar1.visibility = View.INVISIBLE
                        Toast.makeText(
                            requireActivity(),
                            "Please check your internet connection!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

}