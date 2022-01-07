package com.android.example.messapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.android.example.messapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val getActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                binding.progressBar1.visibility = View.VISIBLE
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account?.idToken!!)
                } catch (e: ApiException) {
                    binding.progressBar1.visibility = View.GONE
                    Toast.makeText(this@LoginActivity, "Sign In failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        binding.googleSignIn.setOnClickListener { signIn() }
        createRequest()
    }

    private fun createRequest() {
        lifecycleScope.launch{
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this@LoginActivity, gso)
        }
    }

    private fun signIn() {
        lifecycleScope.launch{
            val signInIntent = googleSignInClient.signInIntent
            getActivityResult.launch(signInIntent)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        lifecycleScope.launch{
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this@LoginActivity) { task ->
                    if (task.isSuccessful) {
                        binding.progressBar1.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, "Signed in successfully!", Toast.LENGTH_SHORT).show()

                        // start home activity
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    } else {
                        binding.progressBar1.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, "Sign In failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}