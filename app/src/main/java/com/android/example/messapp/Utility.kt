package com.android.example.messapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


@OptIn(ExperimentalCoroutinesApi::class)
fun firebaseUserFlow(): Flow<FirebaseUser?> = callbackFlow {
    val firebaseAuth = FirebaseAuth.getInstance()
    val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        trySend(auth.currentUser)
    }
    firebaseAuth.addAuthStateListener(authStateListener)
    awaitClose {
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}