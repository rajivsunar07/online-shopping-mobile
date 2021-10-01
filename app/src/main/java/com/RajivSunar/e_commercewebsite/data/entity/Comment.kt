package com.RajivSunar.e_commercewebsite.data.entity

import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

data class Comment(
    var product: String? = null,
    var user: User? = null,
    var description: String? = null,
    var created_at: String? = null,
    var updated_at: String? = null
) {
    @PrimaryKey(autoGenerate = false)
    @NotNull
    var _id: String = ""
}
