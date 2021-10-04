package com.rajivsunar.hamroshopwear

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajivsunar.hamroshopwear.api.ServiceBuilder
import com.rajivsunar.hamroshopwear.entity.OrderItem
import com.rajivsunar.hamroshopwear.entity.Product
import com.rajivsunar.hamroshopwear.repository.OrderRepository
import com.rajivsunar.hamroshopwear.repository.ProductRepository
import com.rajivsunar.hamroshopwear.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardActivity : Activity() {
    var itemCount = 0
    var productCount = 0

    private lateinit var tvItems: TextView
    private lateinit var tvProduct: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        getItems()
        getForUser()

        tvItems = findViewById(R.id.tvItems)
        tvProduct = findViewById(R.id.tvProduct)


    }

    fun getItems() {
        CoroutineScope(Dispatchers.IO).launch {

            try{
                val repository = OrderRepository()
                val response = repository.getCart()
                if(response.success == true){
                    withContext(Dispatchers.Main){
                        if(response.result?.size!! > 0){
                            val data = response.result?.get(0)
                            val itemList = ArrayList<OrderItem>()

                            if (data != null) {
                                for(item in data.item!!){
                                    var orderItem = OrderItem()
                                    if (item != null) {
                                        orderItem._id = item._id
                                        orderItem.quantity = item.quantity
                                        orderItem.product = item.product
                                        orderItem.price = item.price
                                        orderItem.seller = item.seller
                                        orderItem.exchangeFor = item.exchangeFor
                                        orderItem._for = item._for
                                        orderItem.created_at = item.created_at
                                        orderItem.updated_at = item.updated_at


                                        itemList.add(orderItem)
                                    }


                                }
                            }

                            itemCount = itemList.size
                            tvItems.text = "Number of items in cart: ${itemCount.toString()}"

                        }



                    }

                }else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@DashboardActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (ex: Exception){
                withContext(Dispatchers.Main){
                    Log.d("Error", ex.toString())

                    Toast.makeText(this@DashboardActivity, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun getForUser() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = ProductRepository()
                val response = repository.getForUser()

                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        if (response.result?.size!! > 0) {
                            var productList = ArrayList<Product>()

                            for (item in response.result!!) {
                                var product = Product()
                                product._id = item._id.toString()
                                product.name = item.name
                                product.description = item.description
                                product.image = item.image as ArrayList<String?>?
                                product.productFor = item.productFor as ArrayList<String?>?
                                product.price = item.price
                                product.user = item.user

                                productList.add(product)
                            }

                            productCount = productList.size
                            tvProduct.text = "Number of my products: ${productCount.toString()}"
                        }


                    }


                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DashboardActivity, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}