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

}