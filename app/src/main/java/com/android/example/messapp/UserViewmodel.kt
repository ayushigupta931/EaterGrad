package com.android.example.messapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import com.google.firebase.firestore.DocumentSnapshot

import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnCompleteListener




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
}