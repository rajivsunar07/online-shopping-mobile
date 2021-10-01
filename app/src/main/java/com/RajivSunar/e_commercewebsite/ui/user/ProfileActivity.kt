package com.RajivSunar.e_commercewebsite.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.entity.User
import com.RajivSunar.e_commercewebsite.data.repository.UserRepository
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text

class ProfileActivity : AppCompatActivity() {
    private lateinit var imgProfile: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvAddress: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnChangePassword: Button

    var user: User = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        imgProfile = findViewById(R.id.imgProfile)
        tvName = findViewById(R.id.tvName)
        tvEmail = findViewById(R.id.tvEmail)
        tvPhone = findViewById(R.id.tvPhone)
        tvAddress = findViewById(R.id.tvAddress)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnChangePassword = findViewById(R.id.btnChangePassword)

        getUserInformation()

        btnUpdate.setOnClickListener {
            val intent = Intent(this@ProfileActivity, ProfileUpdateActivity::class.java)
            intent.putExtra("user", user)
            startActivity(
                intent
            )
        }

        btnChangePassword.setOnClickListener {
            val intent = Intent(this@ProfileActivity, ChangePasswordActivity::class.java)
            intent.putExtra("user", user)
            startActivity(
                intent
            )
        }
    }

    fun getUserInformation() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = UserRepository()
                val response = repository.getUser()
                withContext(Dispatchers.Main) {
                    user = response.user!!
                    Glide.with(this@ProfileActivity)
                        .load(ServiceBuilder.BASE_URL + user.image)
                        .into(imgProfile)

                    tvName.text = user.name.toString()
                    tvEmail.text = user.email.toString()
                    tvPhone.text = user.phone.toString()
                    tvAddress.text = user.address.toString()
                    tvPhone.text = user.phone.toString()

                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ProfileActivity.applicationContext,
                        ex.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


}