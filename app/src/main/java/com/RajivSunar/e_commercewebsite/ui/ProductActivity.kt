package com.RajivSunar.e_commercewebsite.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.ui.adapter.ProductAdapter
import com.RajivSunar.e_commercewebsite.data.db.ProductDB
import com.RajivSunar.e_commercewebsite.data.entity.Product
import com.RajivSunar.e_commercewebsite.data.repository.ProductRepository
import com.google.android.material.navigation.NavigationView
//import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class ProductActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mToggle: ActionBarDrawerToggle
    private lateinit var navView : NavigationView
    private lateinit var productRecyclerView: RecyclerView
    var productList: ArrayList<Product> = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        productRecyclerView = findViewById(R.id.productRecyclerView)
        drawerLayout = findViewById(R.id.drawerlayout)
        navView = findViewById(R.id.navView)
        mToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)

        drawerLayout.addDrawerListener(mToggle)
        mToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener(this)

        displayAll()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(mToggle.onOptionsItemSelected(item)){
            Toast.makeText(this@ProductActivity, item.toString(), Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }


    fun displayAll(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
//                var dbproducts = ProductDB
//                    .getInstance(this@ProductActivity)
//                    .getProductDAO()
//                    .getAllProduct() as ArrayList<Product>

                val repository = ProductRepository()
                val response = repository.getAll()
                if(response.success == true){
                    withContext(Dispatchers.Main){
                        if(response.result?.size!! > 0){
                            for(item in response.result!!){
//                                var exists = true
//                            for(product in dbproducts){
//                                if(product._id == item._id){
//                                    exists = true
//                                }
//                            }
//                            if(!exists){

                                var product = Product()
                                product._id = item._id.toString()
                                product.name = item.name
                                product.description = item.description
                                product.image = item.image as ArrayList<String?>?
                                product.productFor = item.productFor as ArrayList<String?>?
                                product.price = item.price
                                product.user = item.user

                                ProductDB.getInstance(this@ProductActivity).getProductDAO().insert(product)

//                            }
                            }
                        }




                        productList = ProductDB
                            .getInstance(this@ProductActivity)
                            .getProductDAO()
                            .getAllProduct() as ArrayList<Product>

                        val adapter = ProductAdapter(productList, this@ProductActivity)
                        productRecyclerView.layoutManager =
                            LinearLayoutManager(this@ProductActivity)
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
        val intent = Intent(this@ProductActivity, LoginActivity::class.java)
        startActivity(
            intent
        )
        return true
    }


}


