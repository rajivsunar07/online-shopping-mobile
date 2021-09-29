package com.RajivSunar.e_commercewebsite.data.entity.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class Converters {
        @TypeConverter
        fun fromString(value: String?): ArrayList<String?>? {
            val gson: Gson = GsonBuilder().create()
            val listType = object : TypeToken<ArrayList<String>>() {}.type
            return gson.fromJson(value, listType)
        }

        @TypeConverter
        fun fromArrayList(list: ArrayList<String?>?): String {
            val type = object: TypeToken<ArrayList<String?>?>() {}.type
            return Gson().toJson(list, type)
        }

}