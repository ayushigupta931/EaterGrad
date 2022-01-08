package com.android.example.messapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.example.messapp.databinding.FragmentExpensesBinding
import com.android.example.messapp.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ExpensesFragment : Fragment() {

    private lateinit var binding: FragmentExpensesBinding
    private val db = Firebase.firestore
    var brkFastCnt: Int= 30
    var lunchCnt: Int=30
    var dinnerCnt: Int=30


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val auth: FirebaseAuth = Firebase.auth
        val uid = auth.currentUser.toString()
       val docref= db.collection("user"). document(uid). collection ("date")



        docref.whereEqualTo("breakfast", false)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    brkFastCnt=brkFastCnt-1
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
        docref.whereEqualTo("lunch", false)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    lunchCnt=lunchCnt-1
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
        docref.whereEqualTo("dinner", false)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    dinnerCnt=dinnerCnt-1
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
        binding = FragmentExpensesBinding.inflate(inflater,container,false)
        binding.BfastCnt.text = brkFastCnt.toString()
        binding.LunchCnt.text = lunchCnt.toString()
        binding.DinnerCnt.text = dinnerCnt.toString()
        return binding.root

    }


  }
