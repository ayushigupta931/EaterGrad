package com.android.example.messapp

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import com.google.firebase.firestore.DocumentSnapshot

import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.suspendCoroutine


class UserViewmodel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val userRef: CollectionReference = db.collection("users")
    val TAG="MessApp"


    fun isNewUser(uid: String?):Boolean{
        var bool: Boolean=true
        viewModelScope.launch {
            if (uid != null) {
                userRef.document(uid).get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot?> { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document.exists()) {
                            Log.d(TAG, "Document exists!")
                            bool=false
                        } else {
                            Log.d(TAG, "Document does not exist!")
                            bool=true

                        }
                    } else {
                        Log.d(TAG, "Failed with: ", task.exception)
                    }
                })
            }

        }
        return bool
    }

    fun createNewUser(uid: String?, name: String?, email: String?){
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

    fun checkRole(currentUserEmail: String) : Int
    {
        val db = Firebase.firestore
        val admins = db.collection("admin")
        var role = 0

        viewModelScope.launch {
            admins.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        for (doc in document)
                        {
                            val email = doc.getString("email")
                            if(email== currentUserEmail)
                            {
                                role = 1
                                Log.e("Role after email = ", role.toString())
                                break
                            }
                        }
                        if(role == 0)
                            role = 2
                    } else {
                        role = 2
                        Log.d(ContentValues.TAG, "No such document")
                    }
                }
                else {
                    Log.d(TAG, "Failed with: ", task.exception)
                }
            }
        }
        return role
    }

}