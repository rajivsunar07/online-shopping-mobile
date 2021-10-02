package com.RajivSunar.e_commercewebsite.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.repository.UserRepository
import com.RajivSunar.e_commercewebsite.ui.product.ProductActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvForgotPassword: TextView
    private lateinit var btnLogin: Button
    private lateinit var tvNoAccount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvNoAccount = findViewById(R.id.tvNoAccount)

        btnLogin.setOnClickListener {
            if(TextUtils.isEmpty(etEmail.text.toString())){
                etEmail.error = "Enter email address"
                etEmail.requestFocus()
                return@setOnClickListener
            }else if(TextUtils.isEmpty(etPassword.text.toString())){
                etPassword.error = "Enter password"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            login()
        }

        tvNoAccount.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

    }


    private fun login() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()


        CoroutineScope(Dispatchers.IO).launch {

            try{
                val repository = UserRepository()
                val response = repository.login(email, password)
                if(response.success == true){
                    ServiceBuilder.token = "Bearer ${response.token}"

                    val sharedPreferences = getSharedPreferences("emailPasswordPref", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("token", response.token)

                    getUserInformation()

                    startActivity(
                        Intent(this@LoginActivity, ProductActivity::class.java)
                    )
                    finish()
                }else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (ex: Exception){
                withContext(Dispatchers.Main){
                    Log.d("Error", ex.toString())

                    Toast.makeText(this@LoginActivity, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun getUserInformation(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = UserRepository()
                val response = repository.getUser()
                if(response.success == true){
                    withContext(Dispatchers.Main){
                        saveEmailAndPassword(response.user!!._id, response.user!!.name!!)
                    }
                }
            }catch(ex: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@LoginActivity.applicationContext, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveEmailAndPassword(userId: String, userName: String) {
        val sharedPreferences = getSharedPreferences("emailPasswordPref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("email",etEmail.text.toString())
        editor.putString("password",etPassword.text.toString())
        editor.putString("userID",userId)
        editor.putString("userName",userName)

        editor.apply()


    }

}