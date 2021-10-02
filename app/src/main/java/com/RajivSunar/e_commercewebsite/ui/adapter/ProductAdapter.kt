package com.RajivSunar.e_commercewebsite.ui.adapter

import android.content.Context
import android.content.Intent
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
import com.RajivSunar.e_commercewebsite.data.entity.Product
import com.RajivSunar.e_commercewebsite.data.repository.OrderRepository
import com.RajivSunar.e_commercewebsite.ui.product.ProductDetailActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProductAdapter(
    val productList: ArrayList<Product>,
    val context: Context
): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProduct: ImageView = view.findViewById(R.id.imgProduct)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val btnAddToCart: Button = view.findViewById(R.id.btnAddTocart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.design_product, parent, false)

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        if(productList[position] != null){
            val product = productList[position]

            Glide.with(context)
                .load( ServiceBuilder.BASE_URL + product.image!![0])
                .into(holder.imgProduct)

            holder.tvName.text = product.name
            holder.tvPrice.text = "RS. " + product.price.toString()

            holder.imgProduct.setOnClickListener{
                val intent = Intent(this.context, ProductDetailActivity::class.java)
                intent.putExtra("id", product._id)
                context.startActivity(intent)
            }

            holder.btnAddToCart.setOnClickListener {

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
                                Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }catch(ex: Exception){
                        withContext(Dispatchers.Main){
                            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }


    override fun getItemCount(): Int {
        return productList.size
    }

}