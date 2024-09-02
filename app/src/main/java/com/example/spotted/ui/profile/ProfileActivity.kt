package com.example.spotted.ui.profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.spotted.R
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.databinding.ActivityEditProfileBinding
import com.example.spotted.databinding.ActivityProfileBinding
import com.example.spotted.ui.account.Helper

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var userID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userID = intent.getStringExtra("otherId")

        binding.activityProfileImageButtonBack.setOnClickListener {
            finish()
        }

        setupProfile()

        binding.editTextAge.isEnabled = false
        binding.editTextContact.isEnabled = false
        binding.editTextBio.isEnabled = false
        binding.editTextInterest.isEnabled = false
        binding.editTextName.isEnabled = false
    }

    private fun setupProfile(){
        AuthDataService.getUser(userID!!) { user ->
            if (user != null) {
                binding.editTextName.setText(Helper.getValidInformation(user.name))
                binding.editTextInterest.setText(Helper.getValidInformation(user.interests))
                binding.editTextContact.setText(Helper.getValidInformation(user.phone))
                binding.editTextAge.setText(Helper.getAge(user.age))
                binding.editTextBio.setText(Helper.getValidInformation(user.description))
            }
            else{
                Helper.createDialog(this, "Error", "User not found"){}
            }
        }
    }
}