package com.RajivSunar.e_commercewebsite.ui.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.repository.CheckoutRepository
import com.RajivSunar.e_commercewebsite.data.repository.OrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CheckoutActivity : AppCompatActivity() {
    private lateinit var etAddress: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnCheckout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        etAddress = findViewById(R.id.etAddress)
        etPhone = findViewById(R.id.etPhone)
        btnCheckout = findViewById(R.id.btnCheckout)

        btnCheckout.setOnClickListener {
            if (TextUtils.isEmpty(etAddress.text)) {
                etAddress.error = "Enter eddress"
                etAddress.requestFocus()
                return@setOnClickListener
            } else if (TextUtils.isEmpty(etPhone.text)) {
                etPhone.error = "Enter eddress"
                etPhone.requestFocus()
                return@setOnClickListener
            }

            val order_id = intent.getStringExtra("OrderId")
            val total_price = intent.getStringExtra("total_price")

            sendOrder(etAddress.text.toString(), etPhone.text.toString(), order_id.toString(), total_price!!.toInt())


        }
    }

    fun sendOrder(address: String, phone: String, order_id: String, total_price: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = CheckoutRepository()
                val response = repository.insert(address, phone)

                if(response.success == true){
                    withContext(Dispatchers.Main){

                        val orderRepo = OrderRepository()
                        val response = orderRepo.updateOrder(order_id, "ordered", total_price)

                        if(response.success == true){
                            val intent = Intent(this@CheckoutActivity, CartActivity::class.java)
                            startActivity(
                                intent
                            )
                        }

                    }


                }
            }catch(ex: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@CheckoutActivity, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}
