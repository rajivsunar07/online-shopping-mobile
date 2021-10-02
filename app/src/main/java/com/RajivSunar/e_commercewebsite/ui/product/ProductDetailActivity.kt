package com.RajivSunar.e_commercewebsite.ui.product

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.db.ProductDB
import com.RajivSunar.e_commercewebsite.data.entity.Comment
import com.RajivSunar.e_commercewebsite.data.entity.Product
import com.RajivSunar.e_commercewebsite.data.repository.CommentRepository
import com.RajivSunar.e_commercewebsite.data.repository.OrderRepository
import com.RajivSunar.e_commercewebsite.data.repository.ProductRepository
import com.RajivSunar.e_commercewebsite.ui.adapter.CommentAdapter
import com.RajivSunar.e_commercewebsite.ui.adapter.MyProductsAdapter
import com.RajivSunar.e_commercewebsite.ui.adapter.ProductAdapter
import com.RajivSunar.e_commercewebsite.ui.exchangeproduct.ExchangeProductCreateActivity
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
    private lateinit var rvComment: RecyclerView
    private lateinit var etComment: EditText
    private lateinit var btnComment: Button
    private lateinit var btnRequestExchange: Button

    var lstComment = ArrayList<Comment>()
    var product = Product()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        imgProduct = findViewById(R.id.imgProduct)
        tvName = findViewById(R.id.tvName)
        tvPrice = findViewById(R.id.tvPrice)
        tvDescription = findViewById(R.id.tvDescription)
        btnAddTocart = findViewById(R.id.btnAddTocart)
        rvComment = findViewById(R.id.rvComment)
        etComment = findViewById(R.id.etComment)
        btnComment = findViewById(R.id.btnComment)
        btnRequestExchange = findViewById(R.id.btnRequestExchange)

        val id = intent.getStringExtra("id").toString()

        getProductDetails(id)
        getComment(id)

        btnAddTocart.setOnClickListener {
            addToCart()
        }

        btnComment.setOnClickListener {
            if(TextUtils.isEmpty(etComment.text)){
                etComment.error = "Add a comment"
                etComment.requestFocus()
                return@setOnClickListener
            }
            comment(id)
        }

        btnRequestExchange.setOnClickListener {
            val intent = Intent(this@ProductDetailActivity, ExchangeProductCreateActivity::class.java)
            intent.putExtra("exchangeFor",product._id )
            intent.putExtra("seller",product.user )
            startActivity(
                intent
            )
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

    fun getComment(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = CommentRepository()
                val response = repository.getAll(id)
                if(response.success == true){
                    withContext(Dispatchers.Main){
                        val data = response.result

                        if (data != null) {
                            for(comment in data){
                                lstComment.add(comment)
                            }

                        }
                        val adapter = CommentAdapter(lstComment, this@ProductDetailActivity)
                        rvComment.layoutManager =
                            LinearLayoutManager(this@ProductDetailActivity)
                        rvComment.adapter = adapter
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

    fun comment(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = CommentRepository()
                val response = repository.addCommment(
                    product._id,
                    etComment.text.toString()
                )
                if(response.success == true){
                    withContext(Dispatchers.Main){
                        getComment(id)
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