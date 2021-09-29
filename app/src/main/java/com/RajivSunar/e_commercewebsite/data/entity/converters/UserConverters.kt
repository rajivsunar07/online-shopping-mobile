package com.RajivSunar.e_commercewebsite.data.entity.converters

import androidx.room.TypeConverter
import com.RajivSunar.e_commercewebsite.data.entity.User
import com.google.gson.Gson

class UserConverters {
    @TypeConverter
    fun userToJson(value: User?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToUser(value: String): User? {
        return Gson().fromJson(value, User::class.java)
    }
}