package com.RajivSunar.e_commercewebsite.ui.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.db.ProductDB
import com.RajivSunar.e_commercewebsite.data.entity.Product
import com.RajivSunar.e_commercewebsite.data.repository.OrderRepository
import com.RajivSunar.e_commercewebsite.data.repository.ProductRepository
import com.RajivSunar.e_commercewebsite.ui.adapter.ProductAdapter
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var imgProduct: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvDescription: TextView
    private lateinit var btnAddTocart: Button

    var product = Product()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        imgProduct = findViewById(R.id.imgProduct)
        tvName = findViewById(R.id.tvName)
        tvPrice = findViewById(R.id.tvPrice)
        tvDescription = findViewById(R.id.tvDescription)
        btnAddTocart = findViewById(R.id.btnAddTocart)

        val id = intent.getStringExtra("id").toString()

        getProductDetails(id)

        btnAddTocart.setOnClickListener {
            addToCart()
        }
    }


    fun getProductDetails(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = ProductRepository()
                val response = repository.getOne(id)
                if(response.success == true){
                    withContext(Dispatchers.Main){
                        product = response.result?.get(0)!!

                        if (product != null) {
                            Glide.with(this@ProductDetailActivity)
                                .load(ServiceBuilder.BASE_URL + product.image!![0])
                                .into(imgProduct)

                            tvName.text = product.name
                            tvPrice.text = product.price.toString()
                            tvDescription.text = product.description
                        }
                    }


                }
            }catch(ex: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@ProductDetailActivity, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun addToCart() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = OrderRepository()
                val response = repository.addToCart(
                    product._id,
                    1,
                    product.price!!,
                    product.user!!,
                    null,
                    "sell"
                )
                if(response.success == true){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@ProductDetailActivity, response.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }catch(ex: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@ProductDetailActivity, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}