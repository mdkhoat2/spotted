package com.example.spotted.util
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.size
import androidx.fragment.app.Fragment
import com.example.spotted.R
import com.google.android.material.textfield.TextInputLayout

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

    @SuppressLint("ClickableViewAccessibility")
    fun setupUI(fragment: Fragment, rootView: View) {
        if (rootView !is EditText) {
            rootView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_MOVE) {
                    val view = fragment.requireActivity().currentFocus
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
                setupUI(fragment, innerView)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public fun setupUImanual(activity: Activity, rootView: View) {
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

                // Handling Button and ImageButton
                if (v is Button || v is ImageButton) {
                    if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_HOVER_ENTER) {
                        v.alpha = 0.7f
                    } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_HOVER_EXIT) {
                        v.alpha = 1.0f
                    }
                }

                // Handling TextView
                if (v is TextView) {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> v.alpha = 0.7f
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> v.alpha = 1.0f
                    }
                }

                false
            }
        }
    }


    public fun applyVariableFont(fragment: Fragment, view: View, variationSettings: String) {
        val typeface: Typeface? = ResourcesCompat.getFont(fragment.requireContext(), R.font.flex)

        when (view) {
            is TextView -> {
                view.typeface = typeface
                view.setFontVariationSettings(variationSettings)
            }
            is EditText -> {
                view.typeface = typeface
                view.setFontVariationSettings(variationSettings)
            }
            is AppCompatButton -> {
                view.typeface = typeface
                view.setFontVariationSettings(variationSettings)
            }
            is Button -> {
                view.typeface = typeface
                view.setFontVariationSettings(variationSettings)
            }
            is TextInputLayout -> {
                val editText = view.editText
                if (editText != null) {
                    editText.typeface = typeface
                    editText.setFontVariationSettings(variationSettings)
                }
            }
        }
    }

    public fun applyVariableFont(activity: Activity, view: View, variationSettings: String) {
        val typeface: Typeface? = ResourcesCompat.getFont(activity, R.font.flex)

        when (view) {
            is TextView -> {
                view.typeface = typeface
                view.setFontVariationSettings(variationSettings)
            }
            is EditText -> {
                view.typeface = typeface
                view.setFontVariationSettings(variationSettings)
            }
            is AppCompatButton -> {
                view.typeface = typeface
                view.setFontVariationSettings(variationSettings)
            }
            is Button -> {
                view.typeface = typeface
                view.setFontVariationSettings(variationSettings)
            }
            is TextInputLayout -> {
                view.typeface = typeface;
            }
        }
    }

}
