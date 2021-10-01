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
import com.RajivSunar.e_commercewebsite.data.repository.ProductRepository
import com.RajivSunar.e_commercewebsite.ui.product.UpdateProductActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MyProductsAdapter(
    val productList: ArrayList<Product>,
    val context: Context
): RecyclerView.Adapter<MyProductsAdapter.MyProductsViewHolder>() {

    class MyProductsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProduct: ImageView = view.findViewById(R.id.imgProduct)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val btnUpdate: Button = view.findViewById(R.id.btnUpdate)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProductsViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_products_design, parent, false)

        return MyProductsViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyProductsViewHolder, position: Int) {
        if(productList[position] != null){
            val product = productList[position]

            Glide.with(context)
                .load( ServiceBuilder.BASE_URL + product.image!![0])
                .into(holder.imgProduct)

            holder.tvName.text = product.name
            holder.tvPrice.text = "RS. " + product.price.toString()
            holder.tvDescription.text =  product.description.toString()

            holder.btnUpdate.setOnClickListener {
                val intent = Intent(this.context, UpdateProductActivity::class.java)
                intent.putExtra("id", product._id)
                this.context.startActivity(
                    intent
                )
            }


            holder.btnDelete.setOnClickListener {

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repository = ProductRepository()
                        val response = repository.deleteProduct(
                            product._id
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