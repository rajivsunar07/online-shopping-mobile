package com.RajivSunar.e_commercewebsite.ui.user

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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.entity.User
import com.RajivSunar.e_commercewebsite.data.repository.UserRepository
import com.RajivSunar.e_commercewebsite.ui.exchangeproduct.ExchangeProductActivity
import com.RajivSunar.e_commercewebsite.ui.order.CartActivity
import com.RajivSunar.e_commercewebsite.ui.product.CreateProductActivity
import com.RajivSunar.e_commercewebsite.ui.product.ProductActivity
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text

class ProfileActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    SensorEventListener {
    private lateinit var imgProfile: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvAddress: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnChangePassword: Button

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mToggle: ActionBarDrawerToggle
    private lateinit var navView : NavigationView

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null


    var user: User = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        imgProfile = findViewById(R.id.imgProfile)
        tvName = findViewById(R.id.tvName)
        tvEmail = findViewById(R.id.tvEmail)
        tvPhone = findViewById(R.id.tvPhone)
        tvAddress = findViewById(R.id.tvAddress)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnChangePassword = findViewById(R.id.btnChangePassword)

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

        if(ServiceBuilder.token != null){
            navView.menu.findItem(R.id.nav_login).setVisible(false)
            navView.menu.findItem(R.id.nav_logout).setVisible(true)
        }else{
            navView.menu.findItem(R.id.nav_login).setVisible(true)
            navView.menu.findItem(R.id.nav_logout).setVisible(false)
        }

        getUserInformation()

        btnUpdate.setOnClickListener {
            val intent = Intent(this@ProfileActivity, ProfileUpdateActivity::class.java)
            intent.putExtra("user", user)
            startActivity(
                intent
            )
        }

        btnChangePassword.setOnClickListener {
            val intent = Intent(this@ProfileActivity, ChangePasswordActivity::class.java)
            intent.putExtra("user", user)
            startActivity(
                intent
            )
        }
    }

    fun getUserInformation() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = UserRepository()
                val response = repository.getUser()
                withContext(Dispatchers.Main) {
                    user = response.user!!
                    if(user.image != null ){
                        Glide.with(this@ProfileActivity)
                            .load(ServiceBuilder.BASE_URL + user.image)
                            .into(imgProfile)
                    }


                    tvName.text = user.name.toString()
                    tvEmail.text = user.email.toString()

                    if(user.phone.toString() != "null"){
                        tvPhone.text = user.phone.toString()
                    }else{
                        tvPhone.text = ""
                    }

                    if(user.address.toString() != "null"){
                        tvAddress.text = user.address.toString()
                    }else{
                        tvAddress.text = ""
                    }

                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ProfileActivity.applicationContext,
                        ex.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
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
        }else if(item.itemId == R.id.nav_logout) {
            ServiceBuilder.token = null
            val intent = Intent(this, ProductActivity::class.java)
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