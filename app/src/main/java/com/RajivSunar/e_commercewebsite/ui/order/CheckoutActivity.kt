package com.RajivSunar.e_commercewebsite.ui.order


import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.RajivSunar.e_commercewebsite.NotificationChannel
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
            val total_price = intent.getIntExtra("total_price", 0)

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
                            loadNotification()

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

    fun loadNotification() {
        val notificationManager = NotificationManagerCompat.from(this)

        val notificationChannels = NotificationChannel(this)
        notificationChannels.createNotificationChannels()

        val notification = NotificationCompat.Builder(this, notificationChannels.CHANNEL_2)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Order sent")
            .setContentText("The order has been sent")
            .setColor(Color.BLUE)
            .build()

        notificationManager.notify(2, notification)
    }
}