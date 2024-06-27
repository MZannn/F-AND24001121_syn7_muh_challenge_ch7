package com.example.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.database.dao.UserDao
import com.example.domain.model.User


@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract val userDao: UserDao

    companion object {
        private const val DATABASE_NAME = "USER_DATABASE"

        private var INSTANCES: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {
            var instance = INSTANCES
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCES = instance
            }
            return instance
        }

    }
}