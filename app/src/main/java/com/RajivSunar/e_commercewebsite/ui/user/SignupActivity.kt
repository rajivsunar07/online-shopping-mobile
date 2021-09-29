package com.RajivSunar.e_commercewebsite.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.entity.User
import com.RajivSunar.e_commercewebsite.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignupActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etName: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        etEmail = findViewById(R.id.etEmail)
        etName = findViewById(R.id.etName)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val email = etEmail.text.toString()
            val name = etName.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if(password != confirmPassword){
                etPassword.error = "Passwords do not match"
                etPassword.requestFocus()
                return@setOnClickListener
            }else{
                val user = User(email, name, password)
                CoroutineScope(Dispatchers.IO).launch {
//                    UserDB.getInstance(this@SignupActivity).getUserDAO().registerUser(user)
                    try {
                        val repository = UserRepository()
                        val response = repository.register(user)
                        if(response.success == true){
                            withContext(Main){
                                Toast.makeText(this@SignupActivity, "User registered successfully", Toast.LENGTH_SHORT).show()
                            }
                            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                            startActivity(
                                intent
                            )
                        }
                    }catch(ex: Exception){
                        withContext(Main){
                            Toast.makeText(this@SignupActivity, ex.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
//                Toast.makeText(this, "User registered", Toast.LENGTH_SHORT).show()
//                startActivity(
//                    Intent(
//                        this, LoginActivity::class.java
//                    )
//                )
            }

        }
    }

}