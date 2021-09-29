package com.RajivSunar.e_commercewebsite.ui.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.db.ProductDB
import com.RajivSunar.e_commercewebsite.data.entity.Product
import com.RajivSunar.e_commercewebsite.data.repository.ProductRepository
import com.RajivSunar.e_commercewebsite.ui.adapter.MyProductsAdapter
import com.RajivSunar.e_commercewebsite.ui.adapter.ProductAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyProductsActivity : AppCompatActivity() {
    private lateinit var productRecyclerView: RecyclerView

    var productList: ArrayList<Product> = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_products)

        productRecyclerView = findViewById(R.id.productRecyclerView)

        getForUser()
    }


    fun getForUser() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = ProductRepository()
                val response = repository.getForUser()

                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        if (response.result?.size!! > 0) {
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

//                                ProductDB.getInstance(this@MyProductsActivity).getProductDAO()
//                                    .insert(product)


                            }
                        }

                        val adapter = MyProductsAdapter(productList, this@MyProductsActivity)
                        productRecyclerView.layoutManager =
                            LinearLayoutManager(this@MyProductsActivity)
                        productRecyclerView.adapter = adapter

                    }


                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MyProductsActivity, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}