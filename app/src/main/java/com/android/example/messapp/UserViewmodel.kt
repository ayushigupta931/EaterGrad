package com.android.example.messapp

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import com.google.firebase.firestore.DocumentSnapshot

import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class UserViewmodel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val userRef: CollectionReference = db.collection("users")
    private val absentRef = db.collection("absent")
    val TAG = "MessApp"


    fun isNewUser(uid: String?): Boolean {
        var bool: Boolean = true
        viewModelScope.launch {
            if (uid != null) {
                userRef.document(uid).get()
                    .addOnCompleteListener(OnCompleteListener<DocumentSnapshot?> { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document.exists()) {
                                Log.d(TAG, "Document exists!")
                                bool = false
                            } else {
                                Log.d(TAG, "Document does not exist!")
                                bool = true

                            }
                        } else {
                            Log.d(TAG, "Failed with: ", task.exception)
                        }
                    })
            }

        }
        return bool
    }

    fun createNewUser(uid: String?, name: String?, email: String?) {
        val user = hashMapOf(
            "name" to name,
            "email" to email
        )

        viewModelScope.launch {
            if (uid != null) {
                db.collection("users").document(uid)
                    .set(user)
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
            }
        }


    }

    suspend fun checkRole(currentUserEmail: String): Int {
        val db = Firebase.firestore
        val admins = db.collection("admin")
        var role = 0

        return suspendCoroutine {
            admins.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        for (doc in document) {
                            val email = doc.getString("email")
                            if (email == currentUserEmail) {
                                role = 1
                                break
                            }
                        }
                        if (role == 0)
                            role = 2
                    } else {
                        role = 2
                        Log.d(ContentValues.TAG, "No such document")
                    }
                    it.resume(role)
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
            role
        }

    }

    suspend fun setChoice(choice: Boolean, title: String, mDate: mDate?, date: String) {
        viewModelScope.launch {
            firebaseUserFlow().collect { user->
                user?: return@collect

                val brkfast = true
                val lunch = true
                val dinner = true

                var dateObj = mDate
                if(dateObj == null){
                    dateObj = mDate(date,brkfast,lunch,dinner)
                }

                when (title.lowercase()) {
                    "breakfast" -> dateObj.breakfast = choice
                    "lunch" -> dateObj.lunch= choice
                    "dinner" -> dateObj.dinner =choice
                }
                userRef.document(user.uid).collection("date").document(date).set(dateObj).addOnSuccessListener {
                    Log.i("Success","Document updated")
                }.addOnFailureListener{
                    Log.i("Failure","Update fail")
                }

                absentRef.document(date)

            }
        }

    }
}