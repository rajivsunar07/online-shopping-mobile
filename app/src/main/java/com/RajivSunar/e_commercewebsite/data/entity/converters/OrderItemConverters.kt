package com.RajivSunar.e_commercewebsite.data.entity.converters

import androidx.room.TypeConverter
import com.RajivSunar.e_commercewebsite.data.entity.OrderItem
import com.google.gson.Gson

class OrderItemConverters {
    @TypeConverter
    fun listToJson(list: List<OrderItem>?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun jsonToList(value: String): List<OrderItem> {
        return Gson().fromJson(value, Array<OrderItem>::class.java).toList()
    }

}