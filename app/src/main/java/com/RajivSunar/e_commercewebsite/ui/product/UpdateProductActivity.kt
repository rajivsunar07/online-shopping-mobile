package com.RajivSunar.e_commercewebsite.ui.product

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.entity.Product
import com.RajivSunar.e_commercewebsite.data.repository.ProductRepository
import com.RajivSunar.e_commercewebsite.ui.UploadRequestBody
import com.RajivSunar.e_commercewebsite.ui.adapter.MyProductsAdapter
import com.RajivSunar.e_commercewebsite.ui.adapter.ProductImageAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class UpdateProductActivity : AppCompatActivity() {
    companion object{
        var lstImage: ArrayList<String?>? = ArrayList()
        var product: Product = Product()
    }

    private lateinit var btnImage: Button
    private lateinit var etName: EditText
    private lateinit var etDescription: EditText
    private lateinit var etPrice: EditText
    private lateinit var etFor: EditText
    private lateinit var btnUpdate: Button
    private lateinit var imgProduct: ImageView

    private lateinit var productImageRecyclerView: RecyclerView

    private val pickImage = 100
    private var imageUri: Uri? = null

    var imagePathList = ArrayList<String>()

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_product)

        btnImage = findViewById(R.id.btnImage)
        etName = findViewById(R.id.etName)
        etDescription = findViewById(R.id.etDescription)
        etPrice = findViewById(R.id.etPrice)
        etFor = findViewById(R.id.etFor)
        btnUpdate = findViewById(R.id.btnUpdate)
        imgProduct = findViewById(R.id.imgProduct)
        productImageRecyclerView = findViewById(R.id.productImageRecyclerView)

        val id: String? = intent.getStringExtra("id")

        getProduct(id)

        btnImage.setOnClickListener {
            selectPicture()
        }

        btnUpdate.setOnClickListener {
            updateProduct()
        }
    }

    fun selectPicture() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)

        startActivityForResult(gallery, pickImage)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            if (data != null) {
                data?.data?.path?.let { imagePathList.add(it) }
                imageUri = data.data
            }
            imgProduct.setImageURI(data?.data)

        }
    }

    fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""
        val returnCursor = this.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }

    fun getProduct(id: String?){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = ProductRepository()
                val response = repository.getOne(id!!)

                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        if (response.result?.size!! > 0) {
                            product = response.result[0]
                        }

                        lstImage = product.image!!

                        etName.setText(product.name)
                        etDescription.setText(product.description)
                        etPrice.setText(product.price.toString())
                        etFor.setText(product.productFor.toString())


                        val adapter = ProductImageAdapter(lstImage, this@UpdateProductActivity)
                        productImageRecyclerView.layoutManager =
                            LinearLayoutManager(this@UpdateProductActivity, LinearLayoutManager.HORIZONTAL, false)
                        productImageRecyclerView.adapter = adapter

                    }


                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UpdateProductActivity, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun updateProduct(){
        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(imageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(imageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val body = UploadRequestBody(file, "image")
        val multipart = MultipartBody.Part.createFormData(
            "newImages",
            file.name,
            body
        )

        val parts = ArrayList<MultipartBody.Part>()
        parts.add(multipart)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = ProductRepository()
                val response = repository.updateProduct(
                    product._id,
                    parts,
                    lstImage,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), etName.text.toString()),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), etDescription.text.toString()),
                    etPrice.text.toString().toInt(),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), etFor.text.toString())
                )
                if (response.success == true) {

                    val intent = Intent(this@UpdateProductActivity, ProductActivity::class.java)
                    startActivity(
                        intent
                    )
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UpdateProductActivity, ex.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }


    }

}