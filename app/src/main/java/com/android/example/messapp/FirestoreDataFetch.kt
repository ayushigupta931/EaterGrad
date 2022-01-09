package com.android.example.messapp

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Date


class FirestoreDataFetch (application: Application): ViewModel() {
    private val db = Firebase.firestore
    private val dateDao = DateDatabase.getDatabase(application).dateDao()
    var position : Int =-1
    var date : String? = ""

    private val dayFlow = MutableStateFlow("")
    private val menuFlow = dayFlow.map {
        val docRef = db.collection("menu").document(it.lowercase())
        docRef.get().await().toObject<MenuModel>()
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

    val menuUiModelLiveData = menuFlow.combine(dateFlow){menu,date->
        listOf(
            MenuUiModel(
                "Breakfast",
                menu!!.breakfast,
                date?.breakfast?:true
            ),
            MenuUiModel(
                "Lunch",
                menu.lunch,
                date?.lunch?:true
            ),
            MenuUiModel(
                "Dinner",
                menu.dinner,
                date?.dinner?:true
            )
        )

    }.asLiveData()

    fun getMenu(day:String){
        dayFlow.value =day.lowercase()

    }
//    fun setChoice(choiceBf : Boolean,){
//        //ToDo Get choice from user and date from system and change only
//        val choice = hashMapOf(
//            "breakfast" to false,
//            "lunch" to true,
//            "dinner" to false,
//            "date" to "08-01-2022"
//        )
//        viewModelScope.launch {
//            firebaseUserFlow().collect { user->
//                user?:return@collect
//                val db = FirebaseFirestore.getInstance()
//                val TAG="MessApp"
//                val docRef= user.uid.let {
//                    db.collection("users")
//                        .document(it).collection("date")
//                        .document("09-01-2022")
//                }
//                docRef.set(choice).addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!")}
//                    .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
//            }
//        }
//    }

}
class FirebaseDataFetchViewModelFactory(private val application: Application): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FirestoreDataFetch(application) as T
    }



}