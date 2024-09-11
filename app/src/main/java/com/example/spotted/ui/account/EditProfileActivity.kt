package com.example.spotted.ui.account

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.spotted.R
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.backend.dataServices.DataService
import com.example.spotted.databinding.ActivityEditProfileBinding
import com.example.spotted.util.LayoutUtil
import com.example.spotted.util.SupportUtil
import java.io.File
import android.content.Context
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.FileOutputStream
import java.io.InputStream

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    companion object {
        val IMAGE_REQUEST_CODE = 100
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.activityEditProfileImageButtonBack.setOnClickListener {
            finish()
        }
        binding.activityEditProfileAppCompatButtonSaveChanges.setOnClickListener {
            hideKeyboard()
            setupProceed()
        }

        binding.activityEditProfileImageViewChangeAvatar.setOnClickListener{
            pickImageGallery()
        }

        setupAvatar()

        println("After setupAvatar")

        val header : TextView = findViewById(R.id.activityEditProfile_textView_header)
        val saveButton : AppCompatButton = findViewById(R.id.activityEditProfile_appCompatButton_SaveChanges)
        LayoutUtil.applyVariableFont(this,header,"'wght' 500, 'wdth' 150")
        LayoutUtil.applyVariableFont(this,saveButton,"'wght' 500, 'wdth' 150")

        setupProfile()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupProfile(){
        val response = DataService.getAuthProfile()
        if(response != null){
            binding.editTextName.setText(response.name)
            binding.editTextInterest.setText(response.interests)
            binding.editTextContact.setText(response.phone)
            binding.editTextAge.setText(Helper.getAge(response.age))
            binding.editTextBio.setText(response.description)
        }
    }

    private fun hideKeyboard(){
        Helper.hideKeyboard(this, binding.editTextName)
        Helper.hideKeyboard(this, binding.editTextInterest)
        Helper.hideKeyboard(this, binding.editTextContact)
        Helper.hideKeyboard(this, binding.editTextBio)
        Helper.hideKeyboard(this, binding.editTextAge)
    }

    private fun setupProceed(){
        val name = binding.editTextName.text.toString()
        val interest = binding.editTextInterest.text.toString()
        val contact = binding.editTextContact.text.toString()
        val bio = binding.editTextBio.text.toString()
        val age = Helper.getAgeInt(binding.editTextAge.text.toString())

        AuthDataService.updateProfile(name, age, contact, bio, interest){it ->
            if(it != null){
                Helper.createDialog(this, "Success", "Profile has been updated"){}
            }
            else{
                Helper.createDialog(this, "Failed", DataService.getMsg()){}
            }
        }
    }

    private fun setupAvatar(){
        AuthDataService.getAvatar(DataService.getAuthProfile()!!._id){
            if(it != null){
                println(it.url)
                Glide.with(this)
                    .load(it.url)
                    .skipMemoryCache(true)    // Skip memory cache
                    .diskCacheStrategy(DiskCacheStrategy.NONE)  // Skip disk cache
                    .into(binding.activityEditProfileShapeableImageViewAvatar)
            }
        }
    }

    private fun pickImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    fun getFileFromUri(context: Context, uri: Uri): File? {
        // Get the file name from the Uri
        val fileName = uri.lastPathSegment?.split("/")?.lastOrNull()
        fileName?.let {
            // Create a temp file in the cache directory
            val tempFile = File(context.cacheDir, fileName)

            try {
                // Open an input stream from the Uri
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                inputStream?.let { input ->
                    // Write the input stream to the temp file
                    val outputStream = FileOutputStream(tempFile)
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (input.read(buffer).also { length = it } > 0) {
                        outputStream.write(buffer, 0, length)
                    }

                    outputStream.close()
                    input.close()

                    // Return the file
                    return tempFile
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return null
    }

    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                binding.activityEditProfileShapeableImageViewAvatar.setImageURI(uri)
//                val file = File(uri.path!!)
                val file = getFileFromUri(this, uri)
                if (file != null)
                AuthDataService.updateAvatar(file){
                    if(it != null){
                        Helper.createDialog(this, "Success", "Avatar has been updated"){}
                    }
                    else{
                        Helper.createDialog(this, "Failed", DataService.getMsg()){}
                }
            }
        }
    }
}
}