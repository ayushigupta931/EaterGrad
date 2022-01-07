package com.android.example.messapp

import androidx.lifecycle.LiveData


class DateRepository(private val dateDAO: DateDAO) {
    val allDate: LiveData<List<Date>> = dateDAO.getAllDates()


    suspend fun insert(date : Date){
        dateDAO.insert(date)
    }


    suspend fun update(date : Date){
        dateDAO.update(date)
    }


    suspend fun delete(date : Date){
        dateDAO.delete(date)
    }


    suspend fun deleteAll(){
        dateDAO.deleteAll()
    }


    fun getAllDates(): LiveData<List<Date>>{
        return allDate
    }
}