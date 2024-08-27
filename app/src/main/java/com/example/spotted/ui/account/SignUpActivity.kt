package com.example.spotted.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.backend.dataServices.DataService
import com.example.spotted.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        emailFocusListener()
        passwordFocusListener()
        binding.btnSignUp.setOnClickListener{setupSignUp()}
    }

    private fun emailFocusListener()
    {
        binding.emailEditText.setOnFocusChangeListener { _, focused ->
            if(!focused){
                binding.emailContainer.error = Helper.validEmail(binding.emailEditText.text.toString())
            }
        }
    }

    private fun passwordFocusListener()
    {
        val error = Helper.passMatch(binding.passwordEditText.text.toString(), binding.passwordConfirmEditText.text.toString())
        binding.passwordConfirmEditText.setOnFocusChangeListener{_, focused ->
            if(!focused){
                binding.passwordConfirmContainer.error = error
            }
        }
    }

    private fun setupSignUp(){
        Helper.hideKeyboard(this, binding.passwordConfirmEditText)
        Helper.hideKeyboard(this, binding.emailEditText)
        Helper.hideKeyboard(this, binding.passwordEditText)

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
                    Intent(this, LoginActivity::class.java).also{
                        startActivity(it)
                    }
                }
            }
            else{
                Helper.createDialog(this, "Failed", DataService.getMsg()){}
            }
        }
    }
}

