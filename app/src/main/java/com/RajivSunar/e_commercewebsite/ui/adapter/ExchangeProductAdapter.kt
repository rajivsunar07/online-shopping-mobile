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
import com.RajivSunar.e_commercewebsite.data.entity.ExchangeProduct
import com.RajivSunar.e_commercewebsite.data.repository.ExchangeProductRepository
import com.RajivSunar.e_commercewebsite.data.repository.OrderRepository
import com.RajivSunar.e_commercewebsite.data.repository.ProductRepository
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExchangeProductAdapter(
    val lstExchangeProduct: ArrayList<ExchangeProduct>,
    val viewFor: String,
    val context: Context
) : RecyclerView.Adapter<ExchangeProductAdapter.ExchangeProductViewHolder>() {

    class ExchangeProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProduct: ImageView = view.findViewById(R.id.imgProduct)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val imgFor: ImageView = view.findViewById(R.id.imgFor)
        val btn1: Button = view.findViewById(R.id.btn1)
        val btn2: Button = view.findViewById(R.id.btn2)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExchangeProductAdapter.ExchangeProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.design_exchange_product, parent, false)

        return ExchangeProductAdapter.ExchangeProductViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ExchangeProductAdapter.ExchangeProductViewHolder,
        position: Int
    ) {
        if (lstExchangeProduct[position] != null) {
            val exchangeProduct = lstExchangeProduct[position]

            if (viewFor == "user") {
                holder.btn1.setText("Add to cart")
                holder.btn2.setText("Delete")

                if (exchangeProduct.status == "accepted") {
                    holder.btn1.setOnClickListener {
                        addToCart(exchangeProduct)
                    }
                } else {
                    holder.btn1.visibility = View.INVISIBLE
                }

                holder.btn2.setOnClickListener {
                    deleteExchangeProduct(exchangeProduct._id, position)
                }
            } else if (viewFor == "seller") {
                holder.btn1.setText("Accept")
                holder.btn2.setText("Reject")

                holder.btn1.setOnClickListener {
                    changeStatus(exchangeProduct._id, "accepted", position)
                }
                holder.btn2.setOnClickListener {
                    changeStatus(exchangeProduct._id, "rejected", position)
                }
            }

            Glide.with(context.applicationContext)
                .load(ServiceBuilder.BASE_URL + exchangeProduct.image?.get(0))
                .into(holder.imgProduct)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repository = ProductRepository()
                    val response = repository.getOne(exchangeProduct.exchangeFor!!._id)
                    if (response.success == true) {
                        withContext(Dispatchers.Main) {
                            Glide.with(context.applicationContext)
                                .load(ServiceBuilder.BASE_URL + (response.result?.get(0)?.image?.get(0) ?: ""))
                                .into(holder.imgFor)

                        }
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context.applicationContext,
                            ex.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    } }
            }



            holder.tvName.text = exchangeProduct.name
            holder.tvDescription.text = exchangeProduct.description
        }

    }

    private fun deleteExchangeProduct(id: String, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = ExchangeProductRepository()
                val response = repository.deleteExchangeProduct(id)
                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context.applicationContext,
                            response.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()

                        lstExchangeProduct.removeAt(position)
                        notifyItemRemoved(position)
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context.applicationContext,
                        ex.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun changeStatus(id: String, status: String, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = ExchangeProductRepository()
                val response = repository.updateExchangeProduct(id, status)
                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context.applicationContext,
                            response.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()

                        lstExchangeProduct[position].status = status
                        notifyDataSetChanged()
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context.applicationContext,
                        ex.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun addToCart(exchangeProduct: ExchangeProduct) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = OrderRepository()
                val response = repository.addToCart(
                    exchangeProduct.exchangeFor!!._id,
                    1,
                    0,
                    exchangeProduct.exchangeFor!!.user!!,
                    exchangeProduct._id,
                    "exchange"
                )
                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return lstExchangeProduct.size
    }
}

