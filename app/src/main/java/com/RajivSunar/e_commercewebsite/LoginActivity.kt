package com.RajivSunar.e_commercewebsite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.RajivSunar.e_commercewebsite.db.UserDB
import com.RajivSunar.e_commercewebsite.entity.User
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
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvNoAccount = findViewById(R.id.tvNoAccount)

        btnLogin.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        tvNoAccount.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }


    }

    private fun login() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        var user: User? = null
        CoroutineScope(Dispatchers.IO).launch {
            user = UserDB
                .getInstance(this@LoginActivity)
                .getUserDAO()
                .checkUser(email, password)
            if (user == null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                saveUsernameAndPassword()
                startActivity(
                    Intent(
                        this@LoginActivity,
                        DashboardActivity::class.java
                    )
                )
            }
        }

    }

    private fun saveUsernameAndPassword() {
        val sharedPreferences = getSharedPreferences("usernamePasswordPref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("email",etEmail.text.toString())
        editor.putString("password",etPassword.text.toString())

        editor.apply()


    }

}