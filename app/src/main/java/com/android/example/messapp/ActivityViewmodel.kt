package com.android.example.messapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ActivityViewmodel:ViewModel() {


    fun init(application: Application) {
        viewModelScope.launch {
            firebaseUserFlow().collect { user->
                user?:return@collect
                val db = FirebaseFirestore.getInstance()

                val TAG="MessApp"
                val dateViewModel =DateViewModel()
                dateViewModel.init(application)
                val dateRef= user.uid.let {
                    db.collection("users")
                        .document(it).collection("date")
                }
//                val date= Date("08-01-2022",true,false,true)
//                dateViewModel.insert(date)

                print("hiii")
                dateRef.get().addOnSuccessListener { documents ->
                    for (document in documents) {
                        dateViewModel.insert(document.toObject())
                        Log.d(TAG, "${document.id} => ${document.data}")
                    }
                }.addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
            }
        }


    }
}