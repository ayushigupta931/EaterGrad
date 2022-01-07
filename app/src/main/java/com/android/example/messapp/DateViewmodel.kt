package com.android.example.messapp

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DateViewModel(application: Application): AndroidViewModel(application) {
    val alldate: LiveData<List<Date>>
    private val repository: DateRepository
    init {
        val userDao = DateDatabase.getDatabase(application).dateDao()
        repository = DateRepository(userDao)
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