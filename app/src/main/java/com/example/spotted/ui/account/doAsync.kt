package com.example.spotted.ui.account

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import okhttp3.internal.wait

class doAsync (val context: Context, val handler: () -> Boolean) : AsyncTask<Void, Void, Boolean>(){

    private val progressDialog = ProgressDialog(context)

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Void?):  Boolean{
        return handler()
    }

    @Deprecated("Deprecated in Java", ReplaceWith("super.onPreExecute()", "android.os.AsyncTask"))
    override fun onPreExecute() {
        super.onPreExecute()
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    @Deprecated("Deprecated in Java", ReplaceWith("super.onPostExecute(result)", "android.os.AsyncTask"))
    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        progressDialog.dismiss();
    }
}