package com.example.spotted.util
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.view.size

object LayoutUtil {
    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @SuppressLint("ClickableViewAccessibility")
    public fun setupUI(activity: Activity, rootView: View) {
        if (rootView !is EditText) {
            rootView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_MOVE) {
                    val view = activity.currentFocus
                    if (view is EditText) {
                        val scrcoords = IntArray(2)
                        view.getLocationOnScreen(scrcoords)
                        val x = event.rawX + view.left - scrcoords[0]
                        val y = event.rawY + view.top - scrcoords[1]
                        if (x < view.left || x > view.right || y < view.top || y > view.bottom) {
                            hideKeyboard(view)
                            view.clearFocus()
                        }
                    }
                }
                if (v is Button || v is ImageButton) {
                    if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_HOVER_ENTER) {
                        v.alpha = 0.7f
                    } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_HOVER_EXIT) {
                        v.alpha = 1.0f
                    }
                }
                false
            }
        }



        // If a layout container, iterate over children and seed recursion.
        if (rootView is ViewGroup) {
            for (i in 0 until rootView.childCount) {
                val innerView = rootView.getChildAt(i)
                setupUI(activity, innerView)
            }
        }
    }
}
