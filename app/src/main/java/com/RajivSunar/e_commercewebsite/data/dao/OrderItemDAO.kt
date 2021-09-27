package com.RajivSunar.e_commercewebsite.data.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.RajivSunar.e_commercewebsite.data.entity.OrderItem

interface OrderItemDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(orderItem: OrderItem)

    @Query("Select * from OrderItem")
    suspend fun getAllOrderItems():List<OrderItem>

    @Query("Select count(*) from OrderItem")
    suspend fun getCount(): Int?
}