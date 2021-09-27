package com.RajivSunar.e_commercewebsite.data.entity

import android.widget.Toast
import androidx.room.*
import com.RajivSunar.e_commercewebsite.ui.ProductActivity
import org.jetbrains.annotations.NotNull
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList

@Entity(indices = [Index(value = ["_id"], unique = true)])
data class Product(
    var name: String? = null,
    var user: String? = null,
    var price: Int? = null,
    var description: String? = null,
    var image: ArrayList<String?>? = null,
    var productFor: ArrayList<String?>? = null, // 'for' in api database
    var created_at: String? = null,
    var updated_at: String? = null
) {
    @PrimaryKey(autoGenerate = false)
    @NotNull
    var _id: String = ""
}

