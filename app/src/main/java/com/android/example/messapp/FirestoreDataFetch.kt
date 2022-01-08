package com.android.example.messapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class FirestoreDataFetch (): ViewModel() {
    private val db = Firebase.firestore
    private val _menuLiveData = MutableLiveData<MenuModel?>(null)
    val menuLiveData: LiveData<MenuModel?> = _menuLiveData

    fun getMenu(day:String){
        viewModelScope.launch {
            val docRef = db.collection("menu").document(day.lowercase())
            val data = docRef.get().await().toObject<MenuModel>()
            _menuLiveData.value = data
        }
    }
}
