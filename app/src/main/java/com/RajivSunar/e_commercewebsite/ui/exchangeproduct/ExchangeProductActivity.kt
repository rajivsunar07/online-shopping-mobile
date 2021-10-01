package com.RajivSunar.e_commercewebsite.ui.exchangeproduct

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.entity.ExchangeProduct
import com.RajivSunar.e_commercewebsite.data.entity.Product
import com.RajivSunar.e_commercewebsite.data.repository.ExchangeProductRepository
import com.RajivSunar.e_commercewebsite.data.repository.ProductRepository
import com.RajivSunar.e_commercewebsite.ui.adapter.ExchangeProductAdapter
import com.RajivSunar.e_commercewebsite.ui.adapter.ProductAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExchangeProductActivity : AppCompatActivity() {

    private lateinit var rvExchangeProduct: RecyclerView

    var lstExchangeProducts: ArrayList<ExchangeProduct> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_product)

        rvExchangeProduct = findViewById(R.id.rvExchangeProduct)

        val _for = intent.getStringExtra("for")

        getExchangeProducts(_for)

    }

    fun getExchangeProducts(_for: String?){

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = ExchangeProductRepository()
                val response = repository.getExchangeProducts(_for!!)
                if(response.success == true){
                    withContext(Dispatchers.Main){
                        if(response.result?.size!! > 0){
                            for(item in response.result!!){
                                var exchangeProduct = ExchangeProduct()
                                if(item != null){
                                    exchangeProduct._id = item._id
                                    exchangeProduct.name = item.name
                                    exchangeProduct.description = item.description
                                    exchangeProduct.image = item.image
                                    exchangeProduct.exchangeFor = item.exchangeFor
                                    exchangeProduct.seller = item.seller
                                    exchangeProduct.status = item.status
                                    exchangeProduct.created_at = item.created_at
                                    exchangeProduct.updated_at = item.updated_at

                                    lstExchangeProducts.add(exchangeProduct)
                                }
                            }
                        }

                        val adapter = ExchangeProductAdapter( lstExchangeProducts, _for, this@ExchangeProductActivity)
                        rvExchangeProduct.layoutManager = LinearLayoutManager(this@ExchangeProductActivity)
                        rvExchangeProduct.adapter = adapter

                    }

                }
            }catch(ex: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@ExchangeProductActivity, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}