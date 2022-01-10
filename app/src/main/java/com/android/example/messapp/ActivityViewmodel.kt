package com.android.example.messapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.messapp.data.DateViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ActivityViewmodel : ViewModel() {


    fun init(application: Application) {
        viewModelScope.launch {
            firebaseUserFlow().collect { user ->
                user ?: return@collect
                val db = FirebaseFirestore.getInstance()

                val TAG = "MessApp"
                val dateViewModel = DateViewModel()
                dateViewModel.init(application)
                val dateRef = user.uid.let {
                    db.collection("users")
                        .document(it).collection("date")
                }

                dateRef.addSnapshotListener { value, error ->
                    value ?: return@addSnapshotListener
                    for (document in value.documents) {
                        dateViewModel.insert(document.toObject()!!)
                        Log.d(TAG, "${document.id} => ${document.data}")
                    }
                }
            }
        }


    }
}