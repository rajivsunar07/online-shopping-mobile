package com.RajivSunar.e_commercewebsite.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.RajivSunar.e_commercewebsite.data.dao.OrderItemDAO
import com.RajivSunar.e_commercewebsite.data.entity.*
import com.RajivSunar.e_commercewebsite.data.entity.converters.ProductConverters
import com.RajivSunar.e_commercewebsite.data.entity.converters.UserConverters


@Database(
    entities = [(OrderItem::class)],
    version = 1
)
@TypeConverters( value = arrayOf(ProductConverters::class, UserConverters::class))
abstract class OrderItemDB : RoomDatabase() {
    abstract fun getOrderItemDAO(): OrderItemDAO

    //companion object us created so that it can be directly accessed
    companion object {
        @Volatile
        private var instance: OrderItemDB? = null

        //if null new instance is created otherwise same instance is used
        fun getInstance(context: Context): OrderItemDB {
            if (instance == null) {
                synchronized(OrderItemDB::class) {
                    instance=buildDatabase(context)
                }
            }
            return instance!!
        }
        private fun buildDatabase(context: Context): OrderItemDB {
            return Room.databaseBuilder(
                context.applicationContext,
                OrderItemDB::class.java,
                "OrderItemDB"
            ).build()
        }

    }

}