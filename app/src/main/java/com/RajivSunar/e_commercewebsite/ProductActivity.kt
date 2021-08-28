package com.RajivSunar.e_commercewebsite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.RajivSunar.e_commercewebsite.adapter.ProductAdapter
import com.RajivSunar.e_commercewebsite.db.ProductDB
import com.RajivSunar.e_commercewebsite.entity.Product
import com.RajivSunar.e_commercewebsite.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductActivity : AppCompatActivity() {
    private lateinit var productRecyclerView: RecyclerView
    var productList: ArrayList<Product> = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        productRecyclerView = findViewById(R.id.productRecyclerView)

        displayAll()


    }

    fun displayAll(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var dbproducts = ProductDB
                    .getInstance(this@ProductActivity)
                    .getProductDAO()
                    .getAllProduct() as ArrayList<Product>

                val repository = ProductRepository()
                val response = repository.getAll()
                if(response.success == true){
                    withContext(Dispatchers.Main){
                        for(item in response.result!!){
                            var exists = false
                            for(product in dbproducts){
                                if(product.productId == item.productId){
                                    exists = true
                                }
                            }
                            if(!exists){
                                var product = Product()
                                product.productId = item.productId.toString()
                                product.name = item.name
                                product.description = item.description
                                product.image = item.image
                                product.price = item.price
                                product.user = item.user

                                ProductDB.getInstance(this@ProductActivity).getProductDAO().insert(product)

                            }
                        }


                        productList = ProductDB
                            .getInstance(this@ProductActivity)
                            .getProductDAO()
                            .getAllProduct() as ArrayList<Product>

                        val adapter = ProductAdapter(productList, this@ProductActivity)
                        productRecyclerView.layoutManager =
                            LinearLayoutManager(this@ProductActivity)
                        productRecyclerView.adapter = adapter
                    }

                }
            }catch(ex: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@ProductActivity, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}