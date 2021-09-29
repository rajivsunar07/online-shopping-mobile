package com.RajivSunar.e_commercewebsite.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.entity.OrderItem
import com.RajivSunar.e_commercewebsite.data.repository.OrderRepository
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartAdapter(
    val cartItemList: ArrayList<OrderItem>,
    val context: Context
): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgItem: ImageView = view.findViewById(R.id.imgItem)
        val tvQuantity: TextView = view.findViewById(R.id.tvQuantity)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_design, parent, false)

        return CartAdapter.CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartAdapter.CartViewHolder, position: Int) {
        if(cartItemList[position] != null){
            val cartItem = cartItemList[position]

            holder.tvQuantity.text = cartItem.quantity.toString()
            holder.tvPrice.text = cartItem.price.toString()
            Glide.with(context)
                .load( ServiceBuilder.BASE_URL + cartItem.product?.image!![0])
                .into(holder.imgItem)
//            Glide.with(context)
//                .load( ServiceBuilder.BASE_URL + cartItem.image!![0])
//                .into(holder.imgProduct)

//            holder.tvName.text = product.name
//            holder.tvPrice.text = "RS. " + product.price.toString()


        }

    }


    override fun getItemCount(): Int {
        return cartItemList.size
    }

}