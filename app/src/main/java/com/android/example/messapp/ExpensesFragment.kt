package com.android.example.messapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.example.messapp.databinding.FragmentExpensesBinding
import com.android.example.messapp.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class ExpensesFragment : Fragment() {

    private lateinit var binding: FragmentExpensesBinding
    private val db = Firebase.firestore
    private val auth: FirebaseAuth = Firebase.auth
    private val uid = auth.currentUser.toString()
    private val docref= db.collection("user"). document(uid). collection ("date")
    /* Breakfast Expense = 25
       Lunch Expense = 40
       Dinner Expense = 45
     */


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var brkFastCnt = 30
        var lunchCnt = 30
        var dinnerCnt = 30

        lifecycleScope.launchWhenCreated {
            brkFastCnt -= mealFalse("breakfast")
            lunchCnt -= mealFalse("lunch")
            dinnerCnt -= mealFalse("dinner")
        }

        brkFastCnt *= 25
        lunchCnt *= 40
        dinnerCnt *= 45
        val total = brkFastCnt+lunchCnt+dinnerCnt

        binding = FragmentExpensesBinding.inflate(inflater,container,false)

        binding.BfastCnt.text = "₹ " + brkFastCnt.toString()
        binding.LunchCnt.text = "₹ " + lunchCnt.toString()
        binding.DinnerCnt.text = "₹ " + dinnerCnt.toString()
        binding.MthExp.text = "₹ " + total.toString()

        return binding.root
    }

    private suspend fun mealFalse(meal : String):Int {
        var count=0

        return suspendCoroutine {
            docref.whereEqualTo(meal, false)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        count++
                    }
                    it.resume(count)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
            count
        }
    }

  }
