package com.RajivSunar.e_commercewebsite.ui.order

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.db.OrderDB
//import com.RajivSunar.e_commercewebsite.data.db.OrderItemDB
import com.RajivSunar.e_commercewebsite.data.entity.Order
import com.RajivSunar.e_commercewebsite.data.entity.OrderItem
import com.RajivSunar.e_commercewebsite.data.repository.OrderRepository
import com.RajivSunar.e_commercewebsite.ui.adapter.CartAdapter
import com.RajivSunar.e_commercewebsite.ui.exchangeproduct.ExchangeProductActivity
import com.RajivSunar.e_commercewebsite.ui.product.CreateProductActivity
import com.RajivSunar.e_commercewebsite.ui.product.MyProductsActivity
import com.RajivSunar.e_commercewebsite.ui.product.ProductActivity
import com.RajivSunar.e_commercewebsite.ui.user.LoginActivity
import com.RajivSunar.e_commercewebsite.ui.user.ProfileActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    SensorEventListener {

    companion object{
        var order: Order? = Order()
    }
    private lateinit var linear: LinearLayout
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var tvTotalPrice: TextView
    private lateinit var btnOrder: Button

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mToggle: ActionBarDrawerToggle
    private lateinit var navView : NavigationView

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null


    var itemList: ArrayList<OrderItem> = ArrayList<OrderItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        linear = findViewById(R.id.linear)
        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        tvTotalPrice = findViewById(R.id.tvTotalPrice)
        btnOrder = findViewById(R.id.btnOrder)

        drawerLayout = findViewById(R.id.drawerlayout)
        navView = findViewById(R.id.navView)
        mToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager


        if (!checkSensor())
            return
        else {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        drawerLayout.addDrawerListener(mToggle)
        mToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener(this)


        checkLogin()

        getCart()

        btnOrder.setOnClickListener {
            val intent = Intent(this@CartActivity, CheckoutActivity::class.java)
            intent.putExtra("OrderId", order!!._id)
            intent.putExtra("total_price", order!!.total_price)
            startActivity(
                intent
            )
        }
    }

    private fun checkLogin() {
        if(ServiceBuilder.token == null) {
            val intent = Intent(this@CartActivity, LoginActivity::class.java)
            startActivity(
                intent
            )
        }
    }

    private fun getCart() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = OrderRepository()
                val response = repository.getCart()

                if(response.success == true){
                    withContext(Dispatchers.Main){
                        if(response.result?.size!! > 0){
                            val data = response.result?.get(0)

                            if (data != null) {
                                for(item in data.item!!){
                                    var orderItem = OrderItem()
                                    if (item != null) {
                                       orderItem._id = item._id
                                       orderItem.quantity = item.quantity
                                       orderItem.product = item.product
                                       orderItem.price = item.price
                                       orderItem.seller = item.seller
                                       orderItem.exchangeFor = item.exchangeFor
                                       orderItem._for = item._for
                                       orderItem.created_at = item.created_at
                                       orderItem.updated_at = item.updated_at


                                        itemList.add(orderItem)
                                    }


                                }
                            }

                            order = data
                            val adapter = CartAdapter(itemList, this@CartActivity)
                            cartRecyclerView.layoutManager =
                                LinearLayoutManager(this@CartActivity)
                            cartRecyclerView.adapter = adapter

                            tvTotalPrice.text = order?.total_price.toString()
                        }else{

                        }


                    }


                }

            }catch(ex: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@CartActivity, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(mToggle.onOptionsItemSelected(item)){
            return true
        }
        return false
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.nav_my_products){
            val intent = Intent(this, MyProductsActivity::class.java)
            startActivity(
                intent
            )
        }else if(item.itemId == R.id.nav_login) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(
                intent
            )
        }else if(item.itemId == R.id.nav_home) {
            val intent = Intent(this, ProductActivity::class.java)
            startActivity(
                intent
            )
        }else if(item.itemId == R.id.nav_create_product) {
            val intent = Intent(this, CreateProductActivity::class.java)
            startActivity(
                intent
            )
        }else if(item.itemId == R.id.nav_profile) {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(
                intent
            )
        }else if(item.itemId == R.id.nav_exchange_request_buyer) {
            val intent = Intent(this, ExchangeProductActivity::class.java)
            intent.putExtra("for", "user")
            startActivity(
                intent
            )
        }else if(item.itemId == R.id.nav_exchange_request_seller) {
            val intent = Intent(this, ExchangeProductActivity::class.java)
            intent.putExtra("for", "seller")
            startActivity(
                intent
            )
        }else if(item.itemId == R.id.nav_cart){
            val intent = Intent(this, CartActivity::class.java)
            startActivity(
                intent
            )
        }
        return true
    }


    private fun checkSensor(): Boolean {
        var flag = true
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == null) {
            flag = false
        }
        return flag
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values[1]
        if (values < 0)
            drawerLayout.closeDrawer(Gravity.LEFT)
        else if (values > 0)
            drawerLayout.openDrawer(Gravity.LEFT)

    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}