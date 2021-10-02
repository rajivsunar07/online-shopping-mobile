package com.RajivSunar.e_commercewebsite.ui.adapter

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.entity.Order
import com.RajivSunar.e_commercewebsite.data.entity.OrderItem
import com.RajivSunar.e_commercewebsite.data.repository.CommentRepository
import com.RajivSunar.e_commercewebsite.data.repository.OrderRepository
import com.RajivSunar.e_commercewebsite.ui.order.CartActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartAdapter(
    val cartItemList: ArrayList<OrderItem>?,
    val context: Context
): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgItem: ImageView = view.findViewById(R.id.imgItem)
        val tvQuantity: TextView = view.findViewById(R.id.tvQuantity)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val btnDecrease: ImageButton = view.findViewById(R.id.btnDecrease)
        val btnIncrease: ImageButton = view.findViewById(R.id.btnIncrease)
        val exchangeLayout: LinearLayout = view.findViewById(R.id.exchangeLayout)
        val tvExchangeProduct: TextView = view.findViewById(R.id.tvExchangeProduct)
        val imgExchangeProduct: ImageView = view.findViewById(R.id.imgExchangeProduct)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_design, parent, false)

        return CartAdapter.CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartAdapter.CartViewHolder, position: Int) {
        if(cartItemList!![position] != null){
            val cartItem = cartItemList[position]

            holder.tvQuantity.text = cartItem.quantity.toString()
            holder.tvPrice.text = cartItem.price.toString()
            Glide.with(context)
                .load( ServiceBuilder.BASE_URL + cartItem.product?.image!![0])
                .into(holder.imgItem)

            holder.btnDecrease.setOnClickListener {
                val quantity = cartItem.quantity?.minus(1)
                val price = (cartItem.price!! / cartItem.quantity!!) * quantity!!
                if(quantity < 1){
                    return@setOnClickListener
                }
                var total_price = CartActivity.order!!.total_price?.minus(cartItem.price!!)
                    ?.plus(price)

                updateOrderItem(cartItem._id, quantity, price,position)
                udpateOrder(total_price, CartActivity.order!!._id,  CartActivity.order!!.status.toString())
            }

            holder.btnIncrease.setOnClickListener {
                val quantity = cartItem.quantity?.plus(1)
                val price = (cartItem.price!! / cartItem.quantity!!) * quantity!!
                var total_price = CartActivity.order!!.total_price?.minus(cartItem.price!!)
                    ?.plus(price)
                if(quantity < 1){

                    return@setOnClickListener
                }

                updateOrderItem(cartItem._id, quantity, price,position)
                udpateOrder(total_price, CartActivity.order!!._id, CartActivity.order!!.status.toString())
            }

            if(cartItem.exchangeFor != null){
                holder.exchangeLayout.visibility = View.VISIBLE

                holder.tvExchangeProduct.visibility = View.VISIBLE
                holder.tvExchangeProduct.text = cartItem.exchangeFor!!.name

                Glide.with(context)
                    .load( ServiceBuilder.BASE_URL + cartItem.exchangeFor?.image!![0])
                    .into(holder.imgExchangeProduct)
            }

            holder.btnDelete.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repository = OrderRepository()
                        val response = repository.deleteOrderItem(cartItem._id)
                        if(response.success == true){
                            withContext(Dispatchers.Main){
                                Toast.makeText(context.applicationContext, response.message, Toast.LENGTH_SHORT)
                                    .show()

                                cartItemList.removeAt(position)

                                notifyItemRemoved(position)
                            }
                        }
                    }catch(ex: Exception){
                        withContext(Dispatchers.Main){
                            Toast.makeText(context.applicationContext, ex.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }

    }

    private fun udpateOrder(totalPrice: Int?, id: String, status: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = OrderRepository()
                val response = repository.updateOrder(id, total_price = totalPrice, status = status)
                if(response.success == true){
                    withContext(Dispatchers.Main){
                        Toast.makeText(context.applicationContext, response.message, Toast.LENGTH_SHORT)
                            .show()

                        CartActivity.order?.total_price = totalPrice

                        notifyDataSetChanged()
                    }
                }
            }catch(ex: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context.applicationContext, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return cartItemList!!.size
    }

    fun updateOrderItem(itemId: String, quantity: Int, price: Int, position: Int){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = OrderRepository()
                val response = repository.updateOrderItem(itemId, quantity, price)
                if(response.success == true){
                    withContext(Dispatchers.Main){
                        Toast.makeText(context.applicationContext, response.message, Toast.LENGTH_SHORT)
                            .show()

                        cartItemList!![position].quantity = quantity
                        cartItemList!![position].price = price

                        notifyDataSetChanged()
                    }
                }
            }catch(ex: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context.applicationContext, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



}