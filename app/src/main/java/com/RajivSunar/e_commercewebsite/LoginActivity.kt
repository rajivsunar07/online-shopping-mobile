package com.RajivSunar.e_commercewebsite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import com.RajivSunar.e_commercewebsite.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.db.UserDB
import com.RajivSunar.e_commercewebsite.entity.User
import com.RajivSunar.e_commercewebsite.repository.UserRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

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

//        var user: User? = null
        CoroutineScope(Dispatchers.IO).launch {
//            user = UserDB
//                .getInstance(this@LoginActivity)
//                .getUserDAO()
////                .checkUser(email, password)
//            if (user == null) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            } else {
//                saveEmailAndPassword()
//                startActivity(
//                    Intent(
//                        this@LoginActivity,
//                        DashboardActivity::class.java
//                    )
//                )
//            }

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

    private fun saveEmailAndPassword() {
        val sharedPreferences = getSharedPreferences("emailPasswordPref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("email",etEmail.text.toString())
        editor.putString("password",etPassword.text.toString())

        editor.apply()


    }

}