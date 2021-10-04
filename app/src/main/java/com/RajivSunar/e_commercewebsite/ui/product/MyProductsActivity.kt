package com.RajivSunar.e_commercewebsite.ui.product

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.db.ProductDB
import com.RajivSunar.e_commercewebsite.data.entity.Product
import com.RajivSunar.e_commercewebsite.data.repository.ProductRepository
import com.RajivSunar.e_commercewebsite.ui.adapter.MyProductsAdapter
import com.RajivSunar.e_commercewebsite.ui.adapter.ProductAdapter
import com.RajivSunar.e_commercewebsite.ui.exchangeproduct.ExchangeProductActivity
import com.RajivSunar.e_commercewebsite.ui.order.CartActivity
import com.RajivSunar.e_commercewebsite.ui.user.LoginActivity
import com.RajivSunar.e_commercewebsite.ui.user.ProfileActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyProductsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    SensorEventListener {
    private lateinit var productRecyclerView: RecyclerView

    var productList: ArrayList<Product> = ArrayList<Product>()

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mToggle: ActionBarDrawerToggle
    private lateinit var navView : NavigationView

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_products)

        productRecyclerView = findViewById(R.id.productRecyclerView)


        drawerLayout = findViewById(R.id.drawerlayout)
        navView = findViewById(R.id.navView)
        mToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager


        if (!checkSensor())
        else {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        drawerLayout.addDrawerListener(mToggle)
        mToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener(this)


        getForUser()
    }


    fun getForUser() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = ProductRepository()
                val response = repository.getForUser()

                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        if (response.result?.size!! > 0) {
                            for (item in response.result!!) {
                                var product = Product()
                                product._id = item._id.toString()
                                product.name = item.name
                                product.description = item.description
                                product.image = item.image as ArrayList<String?>?
                                product.productFor = item.productFor as ArrayList<String?>?
                                product.price = item.price
                                product.user = item.user

                                productList.add(product)

//                                ProductDB.getInstance(this@MyProductsActivity).getProductDAO()
//                                    .insert(product)


                            }
                        }

                        val adapter = MyProductsAdapter(productList, this@MyProductsActivity)
                        productRecyclerView.layoutManager =
                            LinearLayoutManager(this@MyProductsActivity)
                        productRecyclerView.adapter = adapter

                    }


                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MyProductsActivity, ex.toString(), Toast.LENGTH_SHORT).show()
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
        if(item.itemId == R.id.nav_cart){
            val intent = Intent(this, CartActivity::class.java)
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