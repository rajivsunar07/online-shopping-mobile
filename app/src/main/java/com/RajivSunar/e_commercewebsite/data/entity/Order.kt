package com.RajivSunar.e_commercewebsite.data.entity

import androidx.room.*
import org.jetbrains.annotations.NotNull
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList

@Entity(indices = [Index(value = ["_id"], unique = true)])
data class Order(
    var total_price: Int? = null,
    var user: String? = null,
    var status: String? = null,
    var checkout: String? = null,
    var created_at: String? = null,
    var updated_at: String? = null,
    var item: List<OrderItem>? = null
    ) {
    @PrimaryKey(autoGenerate = false)
    @NotNull
    var _id: String = ""
}
