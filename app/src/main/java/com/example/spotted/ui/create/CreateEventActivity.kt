package com.example.spotted.ui.create

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.spotted.R
import com.example.spotted.databinding.ActivityCreateEventBinding
import com.example.spotted.util.LayoutUtil
import com.example.spotted.util.SupportUtil

class CreateEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val createEventViewModel = ViewModelProvider(this).get(CreateEventViewModel::class.java)

        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val root: View = binding.root

        setContentView(root)

        LayoutUtil.setupUI(this, root)

        setupInput(root)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupInput(view: View) {
        // Date EditText
        val dateEditText: EditText = view.findViewById(R.id.DatePicker)

        // Set an onClickListener for the drawable end
        dateEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (dateEditText.right - dateEditText.compoundDrawables[2].bounds.width())) {
                    SupportUtil.showDatePicker(dateEditText, this)
                    return@setOnTouchListener true
                }
            }
            false
        }

        val timeEditText: EditText = view.findViewById(R.id.TimePicker)

        timeEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (timeEditText.right - timeEditText.compoundDrawables[2].bounds.width())) {
                    SupportUtil.showTimePicker(timeEditText, this)
                    return@setOnTouchListener true
                }
            }
            false
        }
    }
}
