package com.example.spotted.ui.profile

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.spotted.R
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.databinding.ActivityEditProfileBinding
import com.example.spotted.databinding.ActivityProfileBinding
import com.example.spotted.ui.account.Helper
import com.example.spotted.util.LayoutUtil

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userID = intent.getStringExtra("otherId")

        val isNeedSent = intent.getBooleanExtra("isNeedSent", false)

        binding.activityProfileImageButtonBack.setOnClickListener {
            finish()
        }

        setupProfile(userID)

        hideButtonSend(isNeedSent)
        LayoutUtil.setupUI(this,binding.root)
    }

    private fun hideButtonSend(isNeedSent: Boolean){
        if(isNeedSent){
            binding.activityProfileAppCompatButtonSend.visibility = View.VISIBLE
        }
        else{
            binding.activityProfileAppCompatButtonSend.visibility = View.GONE
        }
    }

    private fun setupProfile(userID: String?){
        AuthDataService.getUser(userID!!) { user ->
            if (user != null) {
                binding.nameTextViewContent.text = user.name
                binding.interestTextViewContent.text = user.interests
                binding.phoneTextViewContent.text = user.phone
                binding.ageTextViewContent.text = Helper.getAge(user.age)
                binding.bioTextViewContent.text = user.description
            }
            else{
                Helper.createDialog(this, "Error", "User not found"){}
            }
        }
    }
}