package com.example.spotted.ui.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.spotted.MainActivity
import com.example.spotted.R
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.backend.dataServices.DataService
import com.example.spotted.databinding.ActivityLoginBinding
import com.example.spotted.util.LayoutUtil
import com.example.spotted.util.SupportUtil

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
        val forgotPassword : TextView = findViewById(R.id.tv_forgotPassword)
        val signUp : TextView = findViewById(R.id.sign_up)

        LayoutUtil.applyVariableFont(this,forgotPassword,"'wght' 700, 'wdth' 150")
        LayoutUtil.applyVariableFont(this,signUp,"'wght' 700, 'wdth' 150")
        LayoutUtil.setupUImanual(this,forgotPassword)
        LayoutUtil.setupUImanual(this,signUp)
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

    private fun setupSignIn()
    {
        Helper.hideKeyboard(this, binding.emailEditText)
        Helper.hideKeyboard(this, binding.passwordEditText)

        val progress = SupportUtil.createProgressDialog(this)

        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        AuthDataService.login(email, password) { response ->
            progress.dismiss()
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