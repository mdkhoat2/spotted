package com.example.spotted.ui.account

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.backend.dataServices.DataService
import com.example.spotted.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        emailFocusListener()
        binding.btnResetPassword.setOnClickListener{setupResetPassword()}
    }

    private fun emailFocusListener()
    {
        binding.emailEditText.setOnFocusChangeListener { _, focused ->
            if(!focused){
                binding.emailContainer.helperText = Helper.validEmail(binding.emailEditText.text.toString())
            }
        }
    }

    private fun setupResetPassword()
    {
        hideKeyboard(this, binding.emailEditText)

        val email = binding.emailEditText.text.toString()
        AuthDataService.forgotPassword(email) {response ->
            if(response != null){
              createDialog(this, "Success", "Congratulations! Please check your mail and login to your account."){
                  val intent = Intent(this, LoginActivity::class.java)
                  startActivity(intent)
              }
            }
            else{
                createDialog(this, "Failed", DataService.getMsg()){}
            }
        }
    }

    private fun hideKeyboard(context: Context, view: View){
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun createDialog(context: Context, title: String, message: String, callbackOK: () -> Unit){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton("OK") {dialog, which ->
            dialog.dismiss()
            callbackOK()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}