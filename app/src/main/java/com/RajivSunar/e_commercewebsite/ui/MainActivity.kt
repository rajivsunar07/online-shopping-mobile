package com.RajivSunar.e_commercewebsite.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.ui.product.ProductActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        Handler().postDelayed({
            val intent = Intent(this@MainActivity, ProductActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}