package com.RajivSunar.e_commercewebsite.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    var email: String? = null,
    var name: String? = null,
    var password: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var userId: Int = 0
}