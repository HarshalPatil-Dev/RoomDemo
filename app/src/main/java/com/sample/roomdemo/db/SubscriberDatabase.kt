package com.sample.roomdemo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.security.AccessControlContext

@Database(entities = [Subscriber::class],version = 1)
abstract class SubscriberDatabase:RoomDatabase() {

    abstract fun subscriberDAO (): SubscriberDAO

    companion object{
        private var INSTANCE : SubscriberDatabase? = null
        fun getInstance(context: Context): SubscriberDatabase{

            var instance = INSTANCE
            if(instance == null){
                instance = Room.databaseBuilder(context.applicationContext,
                SubscriberDatabase::class.java,
                "subscriber_data_database").build()
            }
            return instance
        }
    }
}