package com.android.example.messapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.example.messapp.models.mDate
import kotlinx.coroutines.flow.Flow

@Dao
interface DateDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mDate: mDate)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(mDate: mDate)

    @Delete
    suspend fun delete(mDate: mDate)

    @Query("DELETE FROM date")
    suspend fun deleteAll()

    @Query("SELECT * FROM date")
    fun getAllDates(): LiveData<List<mDate>>

    @Query("SELECT * FROM date WHERE date = :date")
    fun getDate(date: String): Flow<mDate?>

}