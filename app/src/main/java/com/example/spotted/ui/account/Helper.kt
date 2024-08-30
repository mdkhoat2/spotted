package com.example.spotted.ui.account

import android.content.Context
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog

object Helper {
    fun validEmail(emailText: String): String?
    {
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return "Invalid Email Address"
        }
        return null
    }
    fun passMatch(password: String, passwordConfirm: String): String?
    {
        if(password.isEmpty() && passwordConfirm.isEmpty())
        {
            return "Invalid password and confirmed password"
        }
        if(password.equals(passwordConfirm)){
            return null
        }
        return "Both password didn't match"
    }
    fun createDialog(context: Context, title: String, message: String, callbackOK: () -> Unit){
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
    fun hideKeyboard(context: Context, view: View){
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }
    fun extractString(string: String): List<String>{
        return string.split("\n").map { it.trim() }
    }
    fun concatString(s1: String, s2: String): String{
        return s1 + "\n" + s2
    }
}