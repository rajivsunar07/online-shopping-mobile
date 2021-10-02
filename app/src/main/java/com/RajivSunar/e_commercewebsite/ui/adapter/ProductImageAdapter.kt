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
import com.bumptech.glide.Glide

class ProductImageAdapter(
    val lstImage: ArrayList<String?>?,
    val context: Context
): RecyclerView.Adapter<ProductImageAdapter.ProductImageViewHolder>() {

    class ProductImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProduct: ImageView = view.findViewById(R.id.imgProduct)
        val btnRemove: Button = view.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductImageAdapter.ProductImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.design_product_update_image, parent, false)

        return ProductImageAdapter.ProductImageViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProductImageAdapter.ProductImageViewHolder,
        position: Int
    ) {
        if (lstImage?.get(position) != null) {
            val image = lstImage[position]


            Glide.with(context)
                .load(ServiceBuilder.BASE_URL + image)
                .into(holder.imgProduct)

            holder.btnRemove.setOnClickListener {
                lstImage?.removeAt(position)
            }
        }

    }


    override fun getItemCount(): Int {
            return lstImage!!.size
    }
}

