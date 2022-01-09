package com.android.example.messapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.example.messapp.databinding.FragmentAdminBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class AdminFragment : Fragment() {
    private lateinit var binding: FragmentAdminBinding
    val db = FirebaseFirestore.getInstance()
    val TAG = "MessApp"

    //TODO add signout to admin fragment
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
}