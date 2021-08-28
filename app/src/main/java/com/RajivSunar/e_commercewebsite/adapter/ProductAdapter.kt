package com.RajivSunar.e_commercewebsite.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.entity.Product
import com.bumptech.glide.Glide


class ProductAdapter(
    val productList: ArrayList<Product>,
    val context: Context
): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProduct: ImageView = view.findViewById(R.id.imgProduct)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_design, parent, false)

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        if(productList[position] != null){
            val product = productList[position]

            Glide.with(context)
                .load("http://10.0.2.2:5000/" + product.image)
                .into(holder.imgProduct)

            holder.tvName.text = product.name
            holder.tvPrice.text = "RS. " + product.price.toString()
            holder.tvDescription.text = product.description

        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }
}