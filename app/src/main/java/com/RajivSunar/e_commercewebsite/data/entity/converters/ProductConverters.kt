package com.RajivSunar.e_commercewebsite.data.entity.converters

import androidx.room.TypeConverter
import com.RajivSunar.e_commercewebsite.data.entity.Product
import com.google.gson.Gson

class ProductConverters {
    @TypeConverter
    fun productToJson(value: Product): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToProduct(value: String): Product? {
        return Gson().fromJson(value, Product::class.java)
    }
}