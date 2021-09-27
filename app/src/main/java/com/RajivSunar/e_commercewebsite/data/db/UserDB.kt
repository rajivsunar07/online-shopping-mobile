package com.RajivSunar.e_commercewebsite.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.RajivSunar.e_commercewebsite.data.dao.UserDAO
import com.RajivSunar.e_commercewebsite.data.entity.User

@Database(
    entities = [(User::class)],
    version = 1
)
abstract class UserDB : RoomDatabase() {
    //creating instance for UserDAO
    abstract fun getUserDAO(): UserDAO

    //companion object us created so that it can be directly accessed
    companion object {
        @Volatile
        private var instance: UserDB? = null

        //if null new instance is created otherwise same instance is used
        fun getInstance(context: Context): UserDB {
            if (instance == null) {
                synchronized(UserDB::class) {
                    instance=buildDatabase(context)
                }
            }
            return instance!!
        }
        private fun buildDatabase(context: Context): UserDB {
            return Room.databaseBuilder(
                context.applicationContext,
                UserDB::class.java,
                "UserDB"
            ).build()
        }

    }

}