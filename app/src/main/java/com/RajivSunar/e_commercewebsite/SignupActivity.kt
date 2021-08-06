package com.RajivSunar.e_commercewebsite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.RajivSunar.e_commercewebsite.db.UserDB
import com.RajivSunar.e_commercewebsite.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                    UserDB.getInstance(this@SignupActivity).getUserDAO().registerUser(user)
                }
                Toast.makeText(this, "User registered", Toast.LENGTH_SHORT).show()
                startActivity(
                    Intent(
                        this, LoginActivity::class.java
                    )
                )
            }

        }
    }

}