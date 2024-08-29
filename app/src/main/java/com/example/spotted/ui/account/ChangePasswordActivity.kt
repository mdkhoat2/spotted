package com.example.spotted.ui.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.backend.dataServices.DataService
import com.example.spotted.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnProceed.setOnClickListener {
            hideKeyboard()
            setupProceed()
        }
    }

    private fun hideKeyboard(){
        Helper.hideKeyboard(this, binding.oldPasswordEditText)
        Helper.hideKeyboard(this, binding.newPasswordEditText)
        Helper.hideKeyboard(this, binding.confirmNewPassword)
    }

    private fun setupProceed(){
        val oldPassword = binding.oldPasswordEditText.text.toString()
        val newPassword = binding.newPasswordEditText.text.toString()
        val confirmPassword = binding.confirmNewPassword.text.toString()
        val matchMsg = Helper.passMatch(newPassword, confirmPassword)
        if(matchMsg != null){
            Helper.createDialog(this, "Failed", matchMsg){}
            return
        }
        AuthDataService.resetPassword(oldPassword, newPassword){response ->
            if(response != null){
                Helper.createDialog(this, "Success", "Password has been changed"){}
            }
            else{
                Helper.createDialog(this, "Failed", DataService.getMsg()){}
            }
        }
    }
}