package com.RajivSunar.e_commercewebsite.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.RajivSunar.e_commercewebsite.data.entity.Product

@Dao
interface ProductDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)

    @Query("Select * from Product")
    suspend fun getAllProduct():List<Product>

    @Query("Select count(*) from Product")
    suspend fun getCount(): Int?

}