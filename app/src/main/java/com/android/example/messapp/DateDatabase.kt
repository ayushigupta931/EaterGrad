package com.android.example.messapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Date::class], version = 1, exportSchema = false)
abstract class DateDatabase : RoomDatabase(){
    abstract fun dateDao(): DateDAO
    companion object{
        @Volatile
        private var INSTANCE: DateDatabase? = null

        fun getDatabase(context: Context): DateDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DateDatabase::class.java,
                    "date_database"
                ).build()
                INSTANCE = instance
                return instance

            }
        }



    }
}