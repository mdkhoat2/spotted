package com.example.spotted.ui.create

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.spotted.R
import com.example.spotted.databinding.FragmentCreateEventBinding
import com.example.spotted.util.LayoutUtil
import com.example.spotted.util.SupportUtil
import java.util.Calendar

class CreateEventFragment : Fragment() {

    private var _binding: FragmentCreateEventBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?
    ): View {
        val createEventViewModel = ViewModelProvider(this).get(CreateEventViewModel::class.java)

        _binding = FragmentCreateEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

        LayoutUtil.setupUI(requireActivity(),root)



        setupInput(root)

        return root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupInput(view: View)
    {
        // Date EditText
        val dateEditText: EditText = view.findViewById(R.id.DatePicker)

        // Set an onClickListener for the drawable end
        dateEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (dateEditText.right - dateEditText.compoundDrawables[2].bounds.width())) {
                    SupportUtil.showDatePicker(dateEditText,requireActivity())
                    return@setOnTouchListener true
                }
            }
            false
        }

        val timeEditText: EditText = view.findViewById(R.id.TimePicker)

        timeEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (timeEditText.right - timeEditText.compoundDrawables[2].bounds.width())) {
                    SupportUtil.showTimePicker(timeEditText,requireActivity())
                    return@setOnTouchListener true
                }
            }
            false
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}