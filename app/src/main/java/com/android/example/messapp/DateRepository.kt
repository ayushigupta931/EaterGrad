package com.android.example.messapp

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow


class DateRepository(private val dateDAO: DateDAO) {


    suspend fun insert(mDate : mDate){
        dateDAO.insert(mDate)
    }


    suspend fun update(mDate : mDate){
        dateDAO.update(mDate)
    }


    suspend fun delete(mDate : mDate){
        dateDAO.delete(mDate)
    }


    suspend fun deleteAll(){
        dateDAO.deleteAll()
    }

    suspend fun getMyDate(date: String): mDate? {
        return dateDAO.getMyDate(date)
    }


    fun getAllDates(): LiveData<List<mDate>>{
        return dateDAO.getAllDates()
    }
}