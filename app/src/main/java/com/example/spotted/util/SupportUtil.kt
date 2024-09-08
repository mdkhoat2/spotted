package com.example.spotted.util

import NotificationAdapter
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.spotted.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.io.File
import java.io.InputStream
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

object SupportUtil {
    fun showDatePicker(editText: EditText, context: Context) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context, { view, year, month, dayOfMonth ->
                val monthString = if (month < 9) "0${month + 1}" else "${month + 1}"
                val dayString = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                val selectedDate = "$dayString/$monthString/$year"
                editText.setText(selectedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()


        datePickerDialog.show()
    }

    fun showTimePicker(editText: EditText, context: Context) {
        val c = Calendar.getInstance()

        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)


        val timePickerDialog = TimePickerDialog(
            context, { view, hourOfDay, minute ->
                // force hour to be 2 digits
                val hourString = if (hourOfDay < 10) "0$hourOfDay" else "$hourOfDay"
                // force minute to be 2 digits
                val minuteString = if (minute < 10) "0$minute" else "$minute"
                val selectedTime = "$hourString:$minuteString"
                editText.setText(selectedTime)
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
        val timeNow = System.currentTimeMillis()
        val timeNowCal = Calendar.getInstance()
        timeNowCal.timeInMillis = timeNow
        val timeCal = Calendar.getInstance()
        timeCal.timeInMillis = time.time
        return if (timeNowCal.get(Calendar.DAY_OF_YEAR) == timeCal.get(Calendar.DAY_OF_YEAR) && timeNowCal.get(
                Calendar.YEAR
            ) == timeCal.get(Calendar.YEAR)
        ) {
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val time =
                Instant.ofEpochMilli(time.time).atZone(ZoneId.systemDefault()).toLocalDateTime()
                    .format(timeFormatter)
            time
        } else {
            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            val time =
                Instant.ofEpochMilli(time.time).atZone(ZoneId.systemDefault()).toLocalDateTime()
                    .format(dateFormatter)
            time
        }
    }

    fun SplitDateTimeString(dateTime: String): Pair<String, String> {
        val splitDateTime = dateTime.split(" ")
        // if no date return current date
        if (splitDateTime.size == 1) {
            val currentDate = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val formattedDate = currentDate.format(formatter)
            return Pair(formattedDate, splitDateTime[0])
        }
        return Pair(splitDateTime[0], splitDateTime[1])
    }

    fun getTimestampFromString(date: String, time: String): Timestamp {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val dateTime = "$date $time"
        val localDateTime = LocalDateTime.parse(dateTime, formatter)

        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = localDateTime.format(formatter2)

        val timestamp = Timestamp.valueOf(formattedDateTime)
        return timestamp
    }


    // Use: val progress = SupportUtil.createProgressDialog(this)
    // Use: before doing anything: progress.dismiss()
    fun createProgressDialog(context: Context): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        return progressDialog
    }

    fun createFileFromInputStream(inputStream: InputStream, fileName: String): File {
        val file = File(fileName)
        file.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        return file
    }

    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun getSportIcon(eventType: String): Int {
        // uncapitalized the event type
        val eventTypes = eventType.lowercase(Locale.getDefault())
        // get rid of spaces
        eventTypes.replace(" ", "")

        return when (eventTypes) {
            "badminton" -> R.drawable.badminton
            "baseball" -> R.drawable.baseball
            "basketball" -> R.drawable.basketball
            "volleyball" -> R.drawable.volleyball
            "football","soccer" -> R.drawable.football
            "billiard" -> R.drawable.billiard
            "ping_pong" -> R.drawable.ping_pong
            "tennis" -> R.drawable.tennis
            "bowling" -> R.drawable.bowling
            "golf" -> R.drawable.golf
            else -> R.drawable.basketball
        }
    }

    fun getNotificationType(type: String): Int{
        return when(type){
            "New Request" -> NotificationAdapter.TYPE_APPROVE_NEW
            "Request Accepted" -> NotificationAdapter.TYPE_REQUEST_ACCEPTED
            "Event Created" -> NotificationAdapter.TYPE_CREATE_EVENT
            "Rejected Request" -> NotificationAdapter.TYPE_REQUEST_REJECTED
            else -> NotificationAdapter.TYPE_REQUEST_ACCEPTED
        }
    }
}