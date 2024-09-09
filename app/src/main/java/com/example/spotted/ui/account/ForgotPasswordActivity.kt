package com.example.spotted.ui.account

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.example.spotted.R
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.backend.dataServices.DataService
import com.example.spotted.databinding.ActivityForgotPasswordBinding
import com.example.spotted.util.LayoutUtil

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        emailFocusListener()
        binding.btnResetPassword.setOnClickListener{
            Helper.hideKeyboard(this, binding.emailEditText)
            setupResetPassword()
        }
        binding.activityForgotPasswordImageButtonBack.setOnClickListener{
            finish()
        }
        val header : TextView = findViewById(R.id.activityForgotPassword_textView_header)
        val resetButton : AppCompatButton = findViewById(R.id.btnResetPassword)

        LayoutUtil.applyVariableFont(this,header,"'wght' 500, 'wdth' 150")
        LayoutUtil.applyVariableFont(this,resetButton,"'wght' 500, 'wdth' 150")
        LayoutUtil.setupUI(this,binding.root)
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