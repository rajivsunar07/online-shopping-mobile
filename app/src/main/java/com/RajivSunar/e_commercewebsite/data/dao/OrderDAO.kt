package com.RajivSunar.e_commercewebsite.data.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.RajivSunar.e_commercewebsite.data.entity.Order

interface OrderDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: Order)

    @Query("Select * from Order")
    suspend fun getAllOrders():List<Order>

    @Query("Select count(*) from Order")
    suspend fun getCount(): Int?
}