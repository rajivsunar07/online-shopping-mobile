package com.RajivSunar.e_commercewebsite.ui.user

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.RajivSunar.e_commercewebsite.R
import com.RajivSunar.e_commercewebsite.data.entity.User
import com.RajivSunar.e_commercewebsite.data.repository.UserRepository
import com.RajivSunar.e_commercewebsite.ui.UploadRequestBody
import com.RajivSunar.e_commercewebsite.ui.product.ProductActivity
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

class ProfileUpdateActivity : AppCompatActivity() {

    private lateinit var btnImage: Button
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etAddress: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnUpdate: Button
    private lateinit var imgUser: ImageView

    private val pickImage = 100
    private var imageUri: Uri? = null

    var user: User = User()

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_update)

        btnImage = findViewById(R.id.btnImage)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etAddress = findViewById(R.id.etAddress)
        etPhone = findViewById(R.id.etPhone)
        btnUpdate = findViewById(R.id.btnUpdate)
        imgUser = findViewById(R.id.imgUser)

        val user = intent.getParcelableExtra<User>("user") as User

        etName.setText(user.name.toString())
        etEmail.setText(user.email.toString())
        etAddress.setText(user.address.toString())
        etPhone.setText(user.phone.toString())

        btnImage.setOnClickListener {
            selectPicture()
        }

        btnUpdate.setOnClickListener {
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
                imageUri = data.data
            }
            imgUser.setImageURI(data?.data)

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


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = UserRepository()
                val response = repository.updateUser(
                    multipart,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), etEmail.text.toString()),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), etName.text.toString()),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), etAddress.text.toString()),
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), etPhone.text.toString())
                )
                if (response.success == true) {

                    val intent = Intent(this@ProfileUpdateActivity, ProfileActivity::class.java)
                    startActivity(
                        intent
                    )
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ProfileUpdateActivity, ex.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }


    }
}