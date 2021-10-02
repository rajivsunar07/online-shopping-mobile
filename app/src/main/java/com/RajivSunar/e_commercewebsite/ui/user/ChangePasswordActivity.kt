package com.RajivSunar.e_commercewebsite.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.repository.UserRepository
import com.RajivSunar.e_commercewebsite.ui.product.ProductActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var etPassword: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etNewConfirmPassword: EditText
    private lateinit var btnChangePassword: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        etPassword = findViewById(R.id.etPassword)
        etNewPassword = findViewById(R.id.etNewPassword)
        etNewConfirmPassword = findViewById(R.id.etNewConfirmPassword)
        btnChangePassword = findViewById(R.id.btnChangePassword)

        btnChangePassword.setOnClickListener {
            if(TextUtils.isEmpty(etPassword.text.toString())){
                etPassword.error = "Enter password"
                etPassword.requestFocus()
                return@setOnClickListener
            }else if(TextUtils.isEmpty(etNewPassword.text.toString())){
                etNewPassword.error = "Enter password"
                etNewPassword.requestFocus()
                return@setOnClickListener
            }else if(TextUtils.isEmpty(etNewConfirmPassword.text.toString())){
                etNewConfirmPassword.error = "Enter password"
                etNewConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            if(etNewPassword.text.toString() != etNewConfirmPassword.text.toString()){
                etNewConfirmPassword.error = "Passwords do not match"
                etNewConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            changePasword()
        }
    }

    private fun changePasword() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = UserRepository()
                val response = repository.changePassword(
                    etPassword.text.toString(),
                    etNewPassword.text.toString()
                )
                if (response.success == true) {
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@ChangePasswordActivity.applicationContext, response.message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@ChangePasswordActivity, ProfileActivity::class.java)
                        startActivity(
                            intent
                        )
                    }

                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ChangePasswordActivity, ex.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }
}