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
import com.RajivSunar.e_commercewebsite.data.db.OrderItemDB
import com.RajivSunar.e_commercewebsite.data.entity.Order
import com.RajivSunar.e_commercewebsite.data.entity.OrderItem
import com.RajivSunar.e_commercewebsite.data.repository.OrderRepository
import com.RajivSunar.e_commercewebsite.ui.adapter.CartAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : AppCompatActivity() {
    private lateinit var linear: LinearLayout
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var tvTotalPrice: TextView
    private lateinit var btnOrder: Button


    var itemList: ArrayList<OrderItem> = ArrayList<OrderItem>()
    var order_id: String? = null
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
            intent.putExtra("OrderId", order_id)
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

                var order = Order()


                if(response.success == true){
                    withContext(Dispatchers.Main){
                        if(response.result?.size!! > 0){

                            val data = response.result?.get(0)




                            if (data != null) {
                                for(item in data.item!!){
                                    var orderItem = OrderItem()
                                    if (item != null) {
                                        orderItem._id = item._id
                                        orderItem.price = item.price
                                        orderItem.quantity = item.quantity
                                        orderItem.seller = item.seller
                                        orderItem.exchangeFor = item.exchangeFor
                                        orderItem._for = item._for
                                        orderItem.created_at = item.created_at
                                        orderItem.updated_at = item.updated_at
                                        orderItem.product = item.product

                                        OrderItemDB.getInstance(this@CartActivity).getOrderItemDAO().insert(orderItem)

                                        itemList.add(orderItem)
                                    }


                                }
                            }

                            if (data != null) {
                                order._id = data._id
                                order.item = data.item
                                order.checkout = data.checkout
                                order.total_price = data.total_price
                                order.status = data.status
                                order.checkout = data.checkout
                                order.created_at= data.created_at
                                order.updated_at = data.updated_at
                            }

                            OrderDB.getInstance(this@CartActivity).getOrderDAO().insert(order)

                            order_id = order._id
                        }


//                        itemList = OrderDB
//                            .getInstance(this@CartActivity)
//                            .getOrderDAO()
//                            .getAllProduct() as ArrayList<Product>
////
                        val adapter = CartAdapter(itemList, this@CartActivity)
                        cartRecyclerView.layoutManager =
                            LinearLayoutManager(this@CartActivity)
                        cartRecyclerView.adapter = adapter

                        tvTotalPrice.text = order.total_price.toString()





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