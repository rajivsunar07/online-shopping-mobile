package com.RajivSunar.e_commercewebsite.ui.adapter

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.entity.Comment
import com.RajivSunar.e_commercewebsite.data.entity.OrderItem
import com.RajivSunar.e_commercewebsite.data.repository.CommentRepository
import com.RajivSunar.e_commercewebsite.data.repository.OrderRepository
import com.RajivSunar.e_commercewebsite.data.repository.UserRepository
import com.RajivSunar.e_commercewebsite.ui.user.LoginActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommentAdapter(
    val lstComment: ArrayList<Comment>,
    val context: Context
): RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvComment: TextView = view.findViewById(R.id.tvComment)
        val tvUser: TextView = view.findViewById(R.id.tvUser)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.design_comment, parent, false)

        return CommentAdapter.CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentAdapter.CommentViewHolder, position: Int) {
        if(lstComment[position] != null){
            val comment = lstComment[position]
            holder.tvComment.text = comment.description
            holder.tvUser.text = comment.user?.name
            holder.btnDelete.visibility = View.VISIBLE
            holder.tvDate.text = comment.created_at?.substring(0,9)

            val preferences = context.applicationContext.getSharedPreferences("emailPasswordPref", MODE_PRIVATE)
            var userId = preferences.getString("userID", "")

            if(userId == comment.user!!._id){
                holder.btnDelete.visibility = View.VISIBLE
            }

            holder.btnDelete.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repository = CommentRepository()
                        val response = repository.deleteComment(comment._id)
                        if(response.success == true){
                            withContext(Dispatchers.Main){
                                Toast.makeText(context.applicationContext, response.message, Toast.LENGTH_SHORT)
                                    .show()

                                lstComment.removeAt(position)
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


    override fun getItemCount(): Int {
        return lstComment.size
    }

}