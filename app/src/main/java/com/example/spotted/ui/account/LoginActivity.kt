package com.example.spotted.ui.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.spotted.MainActivity
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.backend.dataServices.DataService
import com.example.spotted.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        emailFocusListener()
        binding.btnSignIn.setOnClickListener{setupSignIn()}
        binding.tvForgotPassword.setOnClickListener{setupForgotPassword()}
        binding.signUp.setOnClickListener{setupSignUp()}
    }

    private fun emailFocusListener()
    {
        binding.emailEditText.setOnFocusChangeListener { _, focused ->
            if(!focused){
                binding.emailContainer.error = Helper.validEmail(binding.emailEditText.text.toString())
            }
        }
    }

    private fun setupSignIn()
    {
        Helper.hideKeyboard(this, binding.emailEditText)
        Helper.hideKeyboard(this, binding.passwordEditText)

        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        AuthDataService.login(email, password) { response ->
            if (response != null) {
                Intent(this, MainActivity::class.java).also{
                    startActivity(it)
                }
            } else {
                Helper.createDialog(this, "Failed", DataService.getMsg()){}
            }
        }
    }

    private fun setupForgotPassword(){
        Intent(this, ForgotPasswordActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun setupSignUp(){
        Intent(this, SignUpActivity::class.java).also {
            startActivity(it)
        }
    }
}