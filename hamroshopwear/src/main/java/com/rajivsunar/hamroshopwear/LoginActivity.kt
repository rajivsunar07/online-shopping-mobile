package com.rajivsunar.hamroshopwear

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.rajivsunar.hamroshopwear.api.ServiceBuilder
import com.rajivsunar.hamroshopwear.databinding.ActivityLoginBinding
import com.rajivsunar.hamroshopwear.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginActivity : Activity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        etEmail = binding.etEmail
        etPassword = binding.etPassword
        btnLogin = binding.btnLogin



        btnLogin.setOnClickListener{
            login()
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

                    startActivity(
                        Intent(this@LoginActivity, DashboardActivity::class.java)
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
}