package com.RajivSunar.e_commercewebsite.ui.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.db.OrderDB
//import com.RajivSunar.e_commercewebsite.data.db.OrderItemDB
import com.RajivSunar.e_commercewebsite.data.entity.Order
import com.RajivSunar.e_commercewebsite.data.entity.OrderItem
import com.RajivSunar.e_commercewebsite.data.repository.OrderRepository
import com.RajivSunar.e_commercewebsite.ui.adapter.CartAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : AppCompatActivity() {

    companion object{
        var order: Order? = Order()
    }
    private lateinit var linear: LinearLayout
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var tvTotalPrice: TextView
    private lateinit var btnOrder: Button

    var itemList: ArrayList<OrderItem> = ArrayList<OrderItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        linear = findViewById(R.id.linear)
        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        tvTotalPrice = findViewById(R.id.tvTotalPrice)
        btnOrder = findViewById(R.id.btnOrder)


        getCart()

        btnOrder.setOnClickListener {
            val intent = Intent(this@CartActivity, CheckoutActivity::class.java)
            intent.putExtra("OrderId", order!!._id)
            intent.putExtra("total_price", order!!.total_price)
            startActivity(
                intent
            )
        }
    }

    private fun getCart() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = OrderRepository()
                val response = repository.getCart()


                if(response.success == true){
                    withContext(Dispatchers.Main){
                        if(response.result?.size!! > 0){
                            val data = response.result?.get(0)

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

                            order = data
                        }

                        val adapter = CartAdapter(itemList, this@CartActivity)
                        cartRecyclerView.layoutManager =
                            LinearLayoutManager(this@CartActivity)
                        cartRecyclerView.adapter = adapter

                        tvTotalPrice.text = order?.total_price.toString()
                    }


                }

            }catch(ex: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@CartActivity, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



}