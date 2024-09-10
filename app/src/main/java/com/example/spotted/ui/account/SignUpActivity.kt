package com.example.spotted.ui.account

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.spotted.R
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.backend.dataServices.DataService
import com.example.spotted.databinding.ActivitySignUpBinding
import com.example.spotted.util.LayoutUtil

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        emailFocusListener()
        binding.btnSignUp.setOnClickListener{
            hideKeyboard()
            setupSignUp()
        }
        binding.activitySignUpImageButtonBack.setOnClickListener{
            finish()
        }

        val header : TextView = findViewById(R.id.activitySignUp_textView_header)
        val signUpButton : AppCompatButton = findViewById(R.id.btnSignUp)

        LayoutUtil.applyVariableFont(this,header,"'wght' 500, 'wdth' 150")
        LayoutUtil.applyVariableFont(this,signUpButton,"'wght' 500, 'wdth' 150")
        LayoutUtil.setupUI(this,binding.root)
    }

    private fun emailFocusListener()
    {
        binding.emailEditText.setOnFocusChangeListener { _, focused ->
            if(!focused){
                binding.emailContainer.error = Helper.validEmail(binding.emailEditText.text.toString())
            }
        }
    }

    private fun hideKeyboard(){
        Helper.hideKeyboard(this, binding.passwordConfirmEditText)
        Helper.hideKeyboard(this, binding.emailEditText)
        Helper.hideKeyboard(this, binding.passwordEditText)
    }

    private fun setupSignUp(){
        val password = binding.passwordEditText.text.toString()
        val confirmPassword = binding.passwordConfirmEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        if (Helper.passMatch(password, confirmPassword) != null)
        {
            Helper.createDialog(this, "Error", "Password and confirm password do not match"){}
            return
        }
        AuthDataService.signUp(email, password) { response ->
            if(response != null){
                Helper.createDialog(this, "Success", "Congratulations! Please check your mail and log in to new account"){
                        finish()
                }
            }
            else{
                Helper.createDialog(this, "Failed", DataService.getMsg()){}
            }
        }
    }
}

