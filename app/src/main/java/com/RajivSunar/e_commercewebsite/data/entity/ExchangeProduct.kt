package com.RajivSunar.e_commercewebsite.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(indices = [Index(value = ["_id"], unique = true)])
data class ExchangeProduct(
    var name: String? = null,
    var user: String? = null,
    var description: String? = null,
    var image: ArrayList<String>? = null,
    var exchangeFor: Product? = null, // 'for' in api database
    var seller: String? = null,
    var status: String? = null,
    var created_at: String? = null,
    var updated_at: String? = null
) {
    @PrimaryKey(autoGenerate = false)
    @NotNull
    var _id: String = ""
}
