package com.example.spotted.ui.account

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.backend.dataServices.DataService
import com.example.spotted.databinding.ActivityChangePasswordBinding
import com.example.spotted.util.LayoutUtil
import com.google.android.material.textfield.TextInputLayout

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.activityChangePasswordAppCompatButtonProceed.setOnClickListener {
            hideKeyboard()
            setupProceed()
        }
        val header : TextView = binding.activityChangePasswordTextViewHeader
        val proceed : AppCompatButton = binding.activityChangePasswordAppCompatButtonProceed

        LayoutUtil.applyVariableFont(this,header,"'wght' 500, 'wdth' 150")
        LayoutUtil.applyVariableFont(this,proceed,"'wght' 500, 'wdth' 150")

        val back : ImageButton = binding.activityChangePasswordImageButtonBack
        back.setOnClickListener {
            finish()
        }
        LayoutUtil.setupUI(this,binding.root)
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