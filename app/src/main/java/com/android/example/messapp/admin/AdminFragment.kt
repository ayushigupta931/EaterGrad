package com.android.example.messapp.admin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.example.messapp.R
import com.android.example.messapp.data.DateViewModel
import com.android.example.messapp.databinding.FragmentAdminBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AdminFragment : Fragment() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: FragmentAdminBinding
    private val dateViewModel by viewModels<DateViewModel>()
    val db = FirebaseFirestore.getInstance()
    val TAG = "MessApp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dateViewModel.init((activity as AppCompatActivity).applicationContext as Application)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.admin_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_sign_out -> {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Sign Out")
                builder.setMessage("Are you sure you want to Sign out?")
                builder.setPositiveButton("Confirm") { _, _ ->
                    dateViewModel.deleteAll()
                    Firebase.auth.signOut()
                    lifecycleScope.launch {
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.web_client_id))
                            .requestEmail()
                            .build()

                        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
                    }
                    googleSignInClient.signOut()
                    findNavController().navigate(R.id.action_adminFragment_to_loginFragment)
                }
                builder.setNegativeButton("Cancel") { dialog, _ ->
                    dialog?.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = "Mess App"
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = sdf.format(Date())

        lifecycleScope.launchWhenCreated {
            val userCount = getUserCount()
            val docRef = db.collection("absent").document(currentDate)
            docRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: ${snapshot.data}")
                    val b = snapshot.getLong("breakfast")?.toInt()
                    val l = snapshot.getLong("lunch")?.toInt()
                    val d = snapshot.getLong("dinner")?.toInt()
                    binding.countBreakfast.text = (userCount - b!!).toString()
                    binding.countLunch.text = (userCount - l!!).toString()
                    binding.countDinner.text = (userCount - d!!).toString()
                } else {
                    Log.d(TAG, "Current data: null")
                }
            }
        }
    }

    private suspend fun getUserCount(): Int {
        val userRef = db.collection("users")
        var count = 0
        return suspendCoroutine {
            userRef.get().addOnSuccessListener { documents ->
                for (document in documents) {
                    count++
                }
                it.resume(count)
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
            count
        }
    }

}