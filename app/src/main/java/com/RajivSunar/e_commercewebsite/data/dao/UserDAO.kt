package com.RajivSunar.e_commercewebsite.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.RajivSunar.e_commercewebsite.data.entity.User

@Dao
interface UserDAO {
    @Insert
    suspend fun registerUser(user: User)

    //for register user
    @Query("Select * from User")
    suspend fun getAllUser():List<User>

    //for login verification
    @Query("Select * from User where email=(:email) and password=(:password)")
    suspend fun checkUser(email:String,password:String) : User
}