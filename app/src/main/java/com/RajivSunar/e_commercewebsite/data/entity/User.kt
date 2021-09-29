package com.RajivSunar.e_commercewebsite.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity
data class User(
    var email: String? = null,
    var name: String? = null,
    var password: String? = null
) {
    @PrimaryKey(autoGenerate = false)
    @NotNull
    var _id: String = ""
}