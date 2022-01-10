package com.android.example.messapp.expenses

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android.example.messapp.databinding.FragmentExpensesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class ExpensesFragment : Fragment() {

    private lateinit var binding: FragmentExpensesBinding
    private val sdf = SimpleDateFormat("dd-MM-yyyy")
    private var currentDate = sdf.format(Date())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpensesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = "Expense"

        currentDate = currentDate.toString()

        val day = currentDate.subSequence(0, 2) as String
        val days = day.toInt()

        var brkFastCnt = days
        var lunchCnt = days
        var dinnerCnt = days

        lifecycleScope.launchWhenCreated {
            brkFastCnt -= mealFalse("breakfast")
            lunchCnt -= mealFalse("lunch")
            dinnerCnt -= mealFalse("dinner")

            brkFastCnt *= 25
            lunchCnt *= 40
            dinnerCnt *= 45
            val total = brkFastCnt + lunchCnt + dinnerCnt

            binding.BfastCnt.text = "₹ " + brkFastCnt.toString()
            binding.LunchCnt.text = "₹ " + lunchCnt.toString()
            binding.DinnerCnt.text = "₹ " + dinnerCnt.toString()
            binding.totalExpense.text = "₹ " + total.toString()
        }

    }

    private suspend fun mealFalse(meal: String): Int {
        val db = Firebase.firestore
        val auth: FirebaseAuth = Firebase.auth
        val uid = auth.currentUser?.uid
        val docref = uid?.let { db.collection("users").document(it).collection("date") }
        var count = 0
        val checkDate = currentDate.subSequence(3, 10) as String
        val checkDayDate = currentDate.subSequence(0, 2) as String
        val checkDay = checkDayDate.toInt()

        return suspendCoroutine {
            docref?.whereEqualTo(meal, false)?.get()?.addOnSuccessListener { documents ->
                for (document in documents) {
                    val dateRange = document.getString("date")
                    val dayDate = dateRange?.subSequence(0, 2) as String
                    val day = dayDate.toInt()
                    val validDate = dateRange.subSequence(3, 10) as String
                    if (day<=checkDay && checkDate == validDate)
                        count++
                }
                it.resume(count)
            }?.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
            count
        }
    }

}
