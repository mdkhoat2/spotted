package com.example.spotted.util

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.EditText
import com.example.spotted.ui.account.doAsync
import java.sql.Timestamp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import kotlin.math.abs

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

    fun getTimeBetween(time1: Timestamp, time2: Timestamp): Long {
        val timeBetween = abs(time2.time - time1.time)
        return timeBetween
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

    fun translateTime(time: Timestamp): String {
        // if same day, show time, else show date and time
        val timeNow = System.currentTimeMillis()
        val timeNowCal = Calendar.getInstance()
        timeNowCal.timeInMillis = timeNow
        val timeCal = Calendar.getInstance()
        timeCal.timeInMillis = time.time
        return if (timeNowCal.get(Calendar.DAY_OF_YEAR) == timeCal.get(Calendar.DAY_OF_YEAR) && timeNowCal.get(Calendar.YEAR) == timeCal.get(Calendar.YEAR)) {
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val time = Instant.ofEpochMilli(time.time).atZone(ZoneId.systemDefault()).toLocalDateTime().format(timeFormatter)
            time
        } else {
            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            val time = Instant.ofEpochMilli(time.time).atZone(ZoneId.systemDefault()).toLocalDateTime().format(dateFormatter)
            time
        }
    }

    fun createAsyncTask(context: Context, handler: () -> Boolean): Boolean{
        val asyncTask = doAsync(context, handler).execute()
        return asyncTask.get()
    }
}