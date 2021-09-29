package com.RajivSunar.e_commercewebsite.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.RajivSunar.e_commercewebsite.data.dao.ProductDAO
import com.RajivSunar.e_commercewebsite.data.entity.converters.Converters
import com.RajivSunar.e_commercewebsite.data.entity.Product

@Database(
    entities = [(Product::class)],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ProductDB : RoomDatabase() {
    //creating instance for ProductDAO
    abstract fun getProductDAO(): ProductDAO

    //companion object us created so that it can be directly accessed
    companion object {
        @Volatile
        private var instance: ProductDB? = null

        //if null new instance is created otherwise same instance is used
        fun getInstance(context: Context): ProductDB {
            if (instance == null) {
                synchronized(ProductDB::class) {
                    instance=buildDatabase(context)
                }
            }
            return instance!!
        }
        private fun buildDatabase(context: Context): ProductDB {
            return Room.databaseBuilder(
                context.applicationContext,
                ProductDB::class.java,
                "ProductDB"
            ).build()
        }

    }

}