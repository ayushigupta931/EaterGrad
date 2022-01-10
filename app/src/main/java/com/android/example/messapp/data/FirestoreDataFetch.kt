package com.android.example.messapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.android.example.messapp.data.DateDatabase
import com.android.example.messapp.models.MenuModel
import com.android.example.messapp.models.MenuUiModel
import com.android.example.messapp.models.mDate
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class FirestoreDataFetch(application: Application) : ViewModel() {
    private val db = Firebase.firestore
    private val dateDao = DateDatabase.getDatabase(application).dateDao()
    var position: Int = -1
    var date: String? = ""

    private val dayFlow = MutableStateFlow("")
    private val menuFlow = dayFlow.map {
        try{
            val docRef = db.collection("menu").document(it.lowercase())
            docRef.get().await().toObject<MenuModel>()
        }
        catch (e:Exception){
            null
        }

    }

    private val dateFlow = dayFlow.flatMapLatest {

        val format: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY

        val days = arrayOfNulls<String>(7)
        for (i in 0..6) {
            days[i] = format.format(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        date = days[position]

        dateDao.getDate(date!!)
    }

    val menuUiModelLiveData = menuFlow.combine(dateFlow) { menu, date ->
        menu?:return@combine null
        listOf(
            MenuUiModel(
                "Breakfast",
                menu!!.breakfast,
                date?.breakfast ?: true
            ),
            MenuUiModel(
                "Lunch",
                menu.lunch,
                date?.lunch ?: true
            ),
            MenuUiModel(
                "Dinner",
                menu.dinner,
                date?.dinner ?: true
            )
        )

    }.asLiveData()

    fun getMenu(day: String) {
        dayFlow.value = day.lowercase()

    }

    fun updateUserPref(position: Int, choice: Boolean) {
        val currentMenu = menuUiModelLiveData.value!!

        val breakfast = if (position == 0) choice else currentMenu[0].coming
        val lunch = if (position == 1) choice else currentMenu[1].coming
        val dinner = if (position == 2) choice else currentMenu[2].coming
        val newMenu = mDate(date!!, breakfast, lunch, dinner)

        viewModelScope.launch {
            var b: Long = 0
            var l: Long = 0
            var d: Long = 0

            if (breakfast != currentMenu[0].coming) {
                b = if (breakfast) -1
                else 1
            }
            if (lunch != currentMenu[1].coming) {
                l = if (lunch) -1
                else 1
            }
            if (dinner != currentMenu[2].coming) {
                d = if (dinner) -1
                else 1
            }
            val docRef = db.collection("absent").document(date!!)


            docRef.set(mapOf("date" to date!!), SetOptions.merge())
            docRef.update(
                "breakfast", FieldValue.increment(b),
                "lunch", FieldValue.increment(l),
                "dinner", FieldValue.increment(d)
            )

            db.collection("users").document(Firebase.auth.currentUser!!.uid)
                .collection("date").document(date!!).set(newMenu).await()
        }
    }
}

class FirebaseDataFetchViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FirestoreDataFetch(application) as T
    }


}