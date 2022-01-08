package com.android.example.messapp

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DateViewModel(): ViewModel() {
    lateinit var alldate: LiveData<List<Date>>
    lateinit var dateDao: DateDAO


    lateinit var repository: DateRepository

    fun init(app: Application) {
        if (::alldate.isInitialized) return
        dateDao = DateDatabase.getDatabase(app).dateDao()
        repository = DateRepository(dateDao)
        alldate = repository.getAllDates()
    }


    fun insert(date: Date) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(date)
    }
    fun update(date: Date) = viewModelScope.launch(Dispatchers.IO){
        repository.update(date)
    }
    fun delete(date: Date) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(date)
    }
    fun deleteAll() = viewModelScope.launch(Dispatchers.IO){
        repository.deleteAll()
    }


}