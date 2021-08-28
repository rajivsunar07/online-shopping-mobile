package com.RajivSunar.e_commercewebsite.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.RajivSunar.e_commercewebsite.dao.ProductDAO
import com.RajivSunar.e_commercewebsite.entity.Product
import com.RajivSunar.e_commercewebsite.entity.User

@Database(
    entities = [(Product::class)],
    version = 1
)
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