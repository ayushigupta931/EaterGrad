package com.android.example.messapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DateDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(date: Date)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(date: Date)

    @Delete
    suspend fun delete(date: Date)

    @Query("DELETE FROM date")
    suspend fun deleteAll()

    @Query("SELECT * FROM date")
    fun getAllDates(): LiveData<List<Date>>
}