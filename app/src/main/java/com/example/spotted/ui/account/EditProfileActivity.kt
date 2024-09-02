package com.example.spotted.ui.account

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.spotted.R
import com.example.spotted.backend.dataModels.User
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.backend.dataServices.DataService
import com.example.spotted.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.activityEditProfileImageButtonBack.setOnClickListener {
            finish()
        }
        binding.activityEditProfileAppCompatButtonSaveChanges.setOnClickListener {
            hideKeyboard()
            setupProceed()
        }

        setupProfile()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupProfile(){
        AuthDataService.getProfile { response ->
            if (response != null) {
                binding.editTextName.setText(response.name)
                binding.editTextInterest.setText(response.interests)
                binding.editTextContact.setText(response.phone)
                binding.editTextAge.setText(Helper.getAge(response.age))
                binding.editTextBio.setText(response.description)
            }
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
}