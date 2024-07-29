package com.example.spotted.ui.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.spotted.MainActivity
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.backend.dataServices.DataService
import com.example.spotted.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var authDataService = AuthDataService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        emailFocusListener()
        binding.btnSignIn.setOnClickListener{setupSignIn()}
    }

    private fun emailFocusListener()
    {
        binding.emailEditText.setOnFocusChangeListener { _, focused ->
            if(!focused){
                binding.emailContainer.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String?
    {
        val emailText = binding.emailEditText.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return "Invalid Email Address"
        }
        return null
    }

    private fun setupSignIn()
    {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        authDataService.login(email, password) { response ->
            if (response != null) {
                Intent(this, MainActivity::class.java).also{
                    startActivity(it)
                }
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                println("Hello" + DataService.getMsg())
            }
        }
    }
}