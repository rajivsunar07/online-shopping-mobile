package com.RajivSunar.e_commercewebsite.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.RajivSunar.e_commercewebsite.entity.Product

@Dao
interface ProductDAO {
    @Insert
    suspend fun insert(product: Product)

    @Query("Select * from Product")
    suspend fun getAllProduct():List<Product>

    @Query("Select count(*) from Product")
    suspend fun getCount(): Int?

}