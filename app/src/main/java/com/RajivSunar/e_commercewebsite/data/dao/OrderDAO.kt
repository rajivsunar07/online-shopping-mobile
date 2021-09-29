package com.RajivSunar.e_commercewebsite.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.RajivSunar.e_commercewebsite.data.entity.Order
import com.RajivSunar.e_commercewebsite.data.entity.OrderItem

@Dao
interface OrderDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: Order)

//   Order @Query("Select item from Order where _id=:id")
//    suspend fun getOrder(id: String): List<OrderItem>

//    @Query("Select count(*) from Order")
//    suspend fun getCount(): Int?
}