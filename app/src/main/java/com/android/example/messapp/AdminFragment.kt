package com.android.example.messapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.example.messapp.databinding.FragmentAdminBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import java.lang.Exception

class AdminFragment : Fragment() {
    private lateinit var binding: FragmentAdminBinding
    val db = FirebaseFirestore.getInstance()
    val TAG = "MessApp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.admin_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sign_out-> {
                Firebase.auth.signOut()
                deleteAppData()
                findNavController().navigate(R.id.action_adminFragment_to_loginFragment)
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
        (activity as AppCompatActivity).supportActionBar?.title = "Today's Count"
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = sdf.format(Date())
        binding.date.text = currentDate.toString()

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
    private fun deleteAppData() {
        try {
            // clearing app data
            val packageName = context?.packageName
            val runtime = Runtime.getRuntime()
            runtime.exec("pm clear $packageName")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}