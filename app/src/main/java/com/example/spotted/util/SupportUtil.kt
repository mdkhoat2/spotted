package com.example.spotted.util

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.EditText
import java.util.Calendar

object SupportUtil {
    fun showDatePicker(editText: EditText,context: Context) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
            editText.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    fun showTimePicker(editText: EditText,context: Context) {
        val c = Calendar.getInstance()

        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)


        val timePickerDialog = TimePickerDialog(
            context,{ view, hourOfDay, minute ->
                editText.setText("$hourOfDay:$minute")
            },
            hour,
            minute,
            false
        )

        timePickerDialog.show()
    }

    fun getTimeSince(time: Long): String {
        val timeSince = System.currentTimeMillis() - time

        return when {
            timeSince < 60000 -> "less than a minute ago"
            timeSince < 3600000 -> "${timeSince / 60000} minutes ago"
            timeSince < 86400000 -> "${timeSince / 3600000} hours ago"
            else -> "${timeSince / 86400000} days ago"
        }
    }
}