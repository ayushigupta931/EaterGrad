package com.android.example.messapp

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DateViewModel(): ViewModel() {
    lateinit var alldate: LiveData<List<mDate>>
    lateinit var dateDao: DateDAO


    lateinit var repository: DateRepository

    fun init(app: Application) {
        if (::alldate.isInitialized) return
        dateDao = DateDatabase.getDatabase(app).dateDao()
        repository = DateRepository(dateDao)
        alldate = repository.getAllDates()
    }


    fun insert(mDate: mDate) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(mDate)
    }
    fun update(mDate: mDate) = viewModelScope.launch(Dispatchers.IO){
        repository.update(mDate)
    }
    fun delete(mDate: mDate) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(mDate)
    }
    fun deleteAll() = viewModelScope.launch(Dispatchers.IO){
        repository.deleteAll()
    }


}