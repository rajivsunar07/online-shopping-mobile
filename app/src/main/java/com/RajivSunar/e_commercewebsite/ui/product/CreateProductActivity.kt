package com.RajivSunar.e_commercewebsite.ui.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.RajivSunar.e_commercewebsite.R
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import android.content.ContentResolver
import android.os.Build
import android.provider.OpenableColumns
import android.text.TextUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.RajivSunar.e_commercewebsite.data.repository.ProductRepository
import com.RajivSunar.e_commercewebsite.ui.UploadRequestBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileInputStream
import java.io.FileOutputStream


class CreateProductActivity : AppCompatActivity() {
    private lateinit var btnImage: Button
    private lateinit var etName: EditText
    private lateinit var etDescription: EditText
    private lateinit var etPrice: EditText
    private lateinit var etFor: EditText
    private lateinit var btnAdd: Button
    private lateinit var imgProduct: ImageView

    private val pickImage = 100
    private var imageUri: Uri? = null

    var imagePathList = ArrayList<String>()

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_product)

        btnImage = findViewById(R.id.btnImage)
        etName = findViewById(R.id.etName)
        etDescription = findViewById(R.id.etDescription)
        etPrice = findViewById(R.id.etPrice)
        etFor = findViewById(R.id.etFor)
        btnAdd = findViewById(R.id.btnAdd)
        imgProduct = findViewById(R.id.imgProduct)

        btnImage.setOnClickListener {
            selectPicture()
        }

        btnAdd.setOnClickListener {
            if (TextUtils.isEmpty(etName.text.toString())) {
                etName.error = "Enter name"
                etName.requestFocus()
                return@setOnClickListener
            }

            uploadImage()
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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun uploadImage() {

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

        val parts = ArrayList<MultipartBody.Part>()
        parts.add(multipart)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = ProductRepository()
                val response = repository.addProduct(
                    parts,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), etName.text.toString()),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), etDescription.text.toString()),
                    etPrice.text.toString().toInt(),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), etFor.text.toString())
                )
                if (response.success == true) {

                    val intent = Intent(this@CreateProductActivity, ProductActivity::class.java)
                    startActivity(
                        intent
                    )
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CreateProductActivity, ex.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }


    }
}