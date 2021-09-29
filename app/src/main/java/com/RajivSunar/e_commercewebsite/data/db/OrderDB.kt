package com.RajivSunar.e_commercewebsite.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.RajivSunar.e_commercewebsite.data.dao.OrderDAO
import com.RajivSunar.e_commercewebsite.data.entity.Order
import com.RajivSunar.e_commercewebsite.data.entity.converters.OrderItemConverters

@Database(
    entities = [(Order::class)],
    version = 1
)
@TypeConverters(OrderItemConverters::class)
abstract class OrderDB : RoomDatabase() {
    abstract fun getOrderDAO(): OrderDAO

    //companion object us created so that it can be directly accessed
    companion object {
        @Volatile
        private var instance: OrderDB? = null

        //if null new instance is created otherwise same instance is used
        fun getInstance(context: Context): OrderDB {
            if (instance == null) {
                synchronized(OrderDB::class) {
                    instance=buildDatabase(context)
                }
            }
            return instance!!
        }
        private fun buildDatabase(context: Context): OrderDB {
            return Room.databaseBuilder(
                context.applicationContext,
                OrderDB::class.java,
                "OrderDB"
            ).build()
        }

    }

}