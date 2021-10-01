package com.RajivSunar.e_commercewebsite.ui.exchangeproduct

import android.content.ContentResolver
import android.content.Intent
import android.media.Image
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
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.repository.ExchangeProductRepository
import com.RajivSunar.e_commercewebsite.data.repository.UserRepository
import com.RajivSunar.e_commercewebsite.ui.UploadRequestBody
import com.RajivSunar.e_commercewebsite.ui.user.ProfileActivity
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

class ExchangeProductCreateActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnImage: Button
    private lateinit var imgProduct: ImageView
    private lateinit var btnCreate: Button

    private val pickImage = 100
    private var imageUri: Uri? = null

    private var user: String? = null
    private var exchangeFor: String? = null
    private var seller: String? = null

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_product_create)

        etName = findViewById(R.id.etName)
        etDescription = findViewById(R.id.etDescription)
        btnImage = findViewById(R.id.btnImage)
        imgProduct = findViewById(R.id.imgProduct)
        btnCreate = findViewById(R.id.btnCreate)

        exchangeFor = intent.getStringExtra("exchangeFor").toString()
        seller = intent.getStringExtra("seller").toString()

        btnImage.setOnClickListener {
            selectPicture()
        }

        btnCreate.setOnClickListener {
            requestExchange()
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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun requestExchange() {

        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(imageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(imageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val body = UploadRequestBody(file, "image")
        val multipart = MultipartBody.Part.createFormData(
            "image",
            file.name,
            body
        )


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = ExchangeProductRepository()
                val response = repository.createExchangeProduct(
                    multipart,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), etName.text.toString()),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), etDescription.text.toString()),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), exchangeFor.toString()),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), seller.toString())
                )
                if (response.success == true) {

                    val intent = Intent(this@ExchangeProductCreateActivity, ProfileActivity::class.java)
                    startActivity(
                        intent
                    )
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ExchangeProductCreateActivity, ex.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }


    }
}