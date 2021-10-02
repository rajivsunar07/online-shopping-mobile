package com.RajivSunar.e_commercewebsite.ui.product

import android.content.Intent
import android.content.res.Resources
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.ui.adapter.ProductAdapter
import com.RajivSunar.e_commercewebsite.data.entity.Product
import com.RajivSunar.e_commercewebsite.data.repository.ProductRepository
import com.RajivSunar.e_commercewebsite.ui.exchangeproduct.ExchangeProductActivity
import com.RajivSunar.e_commercewebsite.ui.order.CartActivity
import com.RajivSunar.e_commercewebsite.ui.user.LoginActivity
import com.RajivSunar.e_commercewebsite.ui.user.ProfileActivity
import com.google.android.material.navigation.NavigationView
//import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class ProductActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    SensorEventListener {


    private lateinit var productRecyclerView: RecyclerView
    var productList: ArrayList<Product> = ArrayList<Product>()

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mToggle: ActionBarDrawerToggle
    private lateinit var navView : NavigationView

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    var width: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        productRecyclerView = findViewById(R.id.productRecyclerView)

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
        width = Resources.getSystem().getDisplayMetrics().widthPixels

        if(ServiceBuilder.token != null){
            navView.menu.findItem(R.id.nav_login).setVisible(false)
            navView.menu.findItem(R.id.nav_logout).setVisible(true)
        }else{
            navView.menu.findItem(R.id.nav_login).setVisible(true)
            navView.menu.findItem(R.id.nav_logout).setVisible(false)
        }


        displayAll()
        login()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(mToggle.onOptionsItemSelected(item)){
            return true
        }
        return false
    }


    fun displayAll(){
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val repository = ProductRepository()
                val response = repository.getAll()
                if(response.success == true){
                    withContext(Dispatchers.Main){
                        if(response.result?.size!! > 0){
                            for(item in response.result!!){

                                var product = Product()
                                product._id = item._id.toString()
                                product.name = item.name
                                product.description = item.description
                                product.image = item.image as ArrayList<String?>?
                                product.productFor = item.productFor as ArrayList<String?>?
                                product.price = item.price
                                product.user = item.user

                                productList.add(product)

                            }
                        }



                        val adapter = ProductAdapter(productList, this@ProductActivity)
                        var spancount = 2
                        if(width!! > 1700){
                            spancount = 3
                        }
                        productRecyclerView.layoutManager =
                            GridLayoutManager(this@ProductActivity, spancount)
                        productRecyclerView.adapter = adapter

                    }


                }
            }catch(ex: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@ProductActivity, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
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
        }else if(item.itemId == R.id.nav_my_products) {
            val intent = Intent(this, MyProductsActivity::class.java)
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
        }else if(item.itemId == R.id.nav_logout) {
            ServiceBuilder.token = null
            val intent = Intent(this, ProductActivity::class.java)
            startActivity(
                intent
            )
        }
        return true
    }

    fun login(){
        val preferences = getSharedPreferences("emailPasswordPref", MODE_PRIVATE)
        var token = preferences.getString("token", "")

        if(token != ""){
            ServiceBuilder.token = token
        }
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


