package com.RajivSunar.e_commercewebsite.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull


@Entity
data class Product(
    var name: String? = null,
    var user: String? = null,
    var price: Int? = null,
    var description: String? = null,
    var image: String? = null,
) {
    @PrimaryKey(autoGenerate = false)
    @NotNull
    var productId: String = ""
}