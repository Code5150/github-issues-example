package com.сode5150.task3_database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.сode5150.mercury_task3_network.data.Issue
import com.сode5150.task3_database.converters.DateTypeConverter
import com.сode5150.task3_database.dao.EntityDAO

@Database(entities = [Issue::class], version = 1)
@TypeConverters(DateTypeConverter::class)
abstract class IssuesDB : RoomDatabase() {
    abstract fun entityDAO(): EntityDAO

    companion object{
        var INSTANCE: IssuesDB? = null

        fun getIssuesDB(context: Context): IssuesDB? {
            if (INSTANCE == null){
                synchronized(IssuesDB::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, IssuesDB::class.java, "IssuesDB").build()
                }
            }
            return INSTANCE
        }

        fun destroyDB(){
            INSTANCE = null
        }
    }
}