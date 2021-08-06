package com.RajivSunar.e_commercewebsite.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.RajivSunar.e_commercewebsite.dao.UserDAO
import com.RajivSunar.e_commercewebsite.entity.User

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

    }

}