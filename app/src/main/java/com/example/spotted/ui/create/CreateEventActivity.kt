package com.example.spotted.ui.create

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.spotted.MainActivity
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

        val header : TextView = binding.activityCreateEventHeaderTextView
        val location : TextView = binding.activityCreateEventLocationTextView
        val proceed : AppCompatButton = binding.activityCreateEventFinishAppCompatButton

        LayoutUtil.applyVariableFont(this,header,"'wght' 500, 'wdth' 150")
        LayoutUtil.applyVariableFont(this,location,"'wght' 500, 'wdth' 150")
        LayoutUtil.applyVariableFont(this,proceed,"'wght' 500, 'wdth' 150")

        val back : ImageButton = binding.activityChangePasswordImageButtonBack
        back.setOnClickListener {
            finish()
        }

        proceed.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("navigateTo", "home")
            startActivity(intent)
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupInput(view: View) {
        // Date EditText
        val dateEditText: EditText = view.findViewById(R.id.activityCreateEvent_date_editText)

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

        val timeEditText: EditText = view.findViewById(R.id.activityCreateEvent_time_editText)

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
