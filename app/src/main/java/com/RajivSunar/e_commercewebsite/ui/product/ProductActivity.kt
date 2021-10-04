package com.RajivSunar.e_commercewebsite.ui.product

import android.R.attr
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
import android.R.attr.y

import android.R.attr.x
import android.view.WindowManager


class ProductActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    SensorEventListener {


    private lateinit var productRecyclerView: RecyclerView
    var productList: ArrayList<Product> = ArrayList<Product>()

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView

    private lateinit var sensorManager: SensorManager
    private var sensorGyro: Sensor? = null
    private var sensorAcelero: Sensor? = null
    private var sensorProxi: Sensor? = null
    private var sensorLight: Sensor? = null

    private val SHAKE_THRESHOLD = 3.25f // m/S**2

    private val MIN_TIME_BETWEEN_SHAKES_MILLISECS = 1000
    private var mLastShakeTime: Long = 0

    var width: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        productRecyclerView = findViewById(R.id.productRecyclerView)

        drawerLayout = findViewById(R.id.drawerlayout)
        navView = findViewById(R.id.navView)
        mToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)

        //sensors

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

//

//         check gyroscope
        if (!checkGyroscope())
        else {
            sensorGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            sensorManager.registerListener(this, sensorGyro, SensorManager.SENSOR_DELAY_NORMAL)
        }


        // check accelerometer
        if (!checkAccelerometer())
        else {
            sensorAcelero = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            sensorManager.registerListener(this, sensorAcelero, SensorManager.SENSOR_DELAY_NORMAL)
        }

        if (!checkProximitySensor())
        else {
            sensorProxi = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            sensorManager.registerListener(this, sensorProxi, SensorManager.SENSOR_DELAY_NORMAL)
        }

        if (!checkLightSensor())
        else {
            sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL)
        }



        drawerLayout.addDrawerListener(mToggle)
        mToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener(this)
        width = Resources.getSystem().getDisplayMetrics().widthPixels

        if (ServiceBuilder.token != null) {
            navView.menu.findItem(R.id.nav_login).setVisible(false)
            navView.menu.findItem(R.id.nav_logout).setVisible(true)
        } else {
            navView.menu.findItem(R.id.nav_login).setVisible(true)
            navView.menu.findItem(R.id.nav_logout).setVisible(false)
        }


        displayAll()
        login()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (mToggle.onOptionsItemSelected(item)) {
            return true
        }
        return false
    }


    fun displayAll() {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val repository = ProductRepository()
                val response = repository.getAll()
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

                            }
                        }


                        val adapter = ProductAdapter(productList, this@ProductActivity)
                        var spancount = 2
                        if (width!! > 1700) {
                            spancount = 3
                        }
                        productRecyclerView.layoutManager =
                            GridLayoutManager(this@ProductActivity, spancount)
                        productRecyclerView.adapter = adapter

                    }


                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ProductActivity, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_cart) {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(
                intent
            )
        } else if (item.itemId == R.id.nav_login) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(
                intent
            )
        } else if (item.itemId == R.id.nav_my_products) {
            val intent = Intent(this, MyProductsActivity::class.java)
            startActivity(
                intent
            )
        } else if (item.itemId == R.id.nav_create_product) {
            val intent = Intent(this, CreateProductActivity::class.java)
            startActivity(
                intent
            )
        } else if (item.itemId == R.id.nav_profile) {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(
                intent
            )
        } else if (item.itemId == R.id.nav_exchange_request_buyer) {
            val intent = Intent(this, ExchangeProductActivity::class.java)
            intent.putExtra("for", "user")
            startActivity(
                intent
            )
        } else if (item.itemId == R.id.nav_exchange_request_seller) {
            val intent = Intent(this, ExchangeProductActivity::class.java)
            intent.putExtra("for", "seller")
            startActivity(
                intent
            )
        } else if (item.itemId == R.id.nav_logout) {
            ServiceBuilder.token = null
            val intent = Intent(this, ProductActivity::class.java)
            startActivity(
                intent
            )
        }
        return true
    }

    fun login() {
        val preferences = getSharedPreferences("emailPasswordPref", MODE_PRIVATE)
        var token = preferences.getString("token", "")

        if (token != "") {
            ServiceBuilder.token = token
        }
    }

    private fun checkGyroscope(): Boolean {
        var flag = true
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == null) {
            flag = false
        }
        return flag
    }

    private fun checkAccelerometer(): Boolean {
        var flag = true
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) {
            flag = false
        }
        return flag
    }

    private fun checkProximitySensor(): Boolean {
        var flag = true
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) == null) {
            flag = false
        }
        return flag
    }

    private fun checkLightSensor(): Boolean {
        var flag = true
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) == null) {
            flag = false
        }
        return flag
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val curTime = System.currentTimeMillis()
            if (curTime - mLastShakeTime > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {
                val x = event!!.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val acceleration = Math.sqrt(
                    Math.pow(x.toDouble(), 2.0) +
                            Math.pow(y.toDouble(), 2.0) +
                            Math.pow(z.toDouble(), 2.0)
                ) - SensorManager.GRAVITY_EARTH


                if (acceleration > SHAKE_THRESHOLD) {
                    mLastShakeTime = curTime

                    if (ServiceBuilder.token != null) {
                        ServiceBuilder.token = null
                        Toast.makeText(this, "Logout successul", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, ProductActivity::class.java)
                        startActivity(
                            intent
                        )
                    }

                }
            }
        } else if (event!!.sensor.type == Sensor.TYPE_GYROSCOPE) {
            val values = event!!.values[1]
            if (values < -1)
                drawerLayout.closeDrawer(Gravity.LEFT)
            else if (values > 1)
                drawerLayout.openDrawer(Gravity.LEFT)
        } else if (event!!.sensor.type == Sensor.TYPE_PROXIMITY) {
            val values = event!!.values[0]

            val params = this.window.attributes

            if (values <= 4) {
                params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                params.screenBrightness = 0.1f
                this.window.attributes = params
            } else{
                params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                params.screenBrightness = 0.9f
                this.window.attributes = params
                this.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }

        }else if (event!!.sensor.type == Sensor.TYPE_LIGHT) {
            val values = event!!.values[0]

            val params = this.window.attributes

            if (values <= 50) {
                params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                params.screenBrightness = 0.2f
                this.window.attributes = params
            } else if (values >= 50 && values < 5000 ) {
                params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                params.screenBrightness = 0.6f
                this.window.attributes = params
            } else if(values > 5000){
                params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                params.screenBrightness = 0.9f
                this.window.attributes = params
                this.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }

        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}


