package com.rajivsunar.hamroshopwear.entity

import androidx.room.*
import org.jetbrains.annotations.NotNull
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList


@Entity(indices = [Index(value = ["_id"], unique = true)])
data class OrderItem(
    var product: Product? = null,
    var quantity: Int? = null,
    var price: Int? = null,
    var seller: User? = null,
    var exchangeFor: String? = null,
    var _for: String? = null, // is 'for' in database
    var created_at: String? = null,
    var updated_at: String? = null
    ) {
    @PrimaryKey(autoGenerate = false)
    @NotNull
    var _id: String = ""
}
