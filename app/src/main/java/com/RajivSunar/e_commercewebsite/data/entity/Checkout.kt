package com.RajivSunar.e_commercewebsite.data.entity

import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

data class Checkout(
    var adress: String? = null,
    var phone: String? = null,
    var updated_at: String? = null,
    var created_at: String? = null
) {
    @PrimaryKey(autoGenerate = false)
    @NotNull
    var _id: String = ""
}



