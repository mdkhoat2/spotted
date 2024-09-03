package com.example.spotted.util

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.spotted.R
import com.example.spotted.ui.account.doAsync
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
import kotlin.math.abs

object SupportUtil {
    fun showDatePicker(editText: EditText, context: Context) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
                // dd/MM/yyyy
                // force month to be 2 digits
                val monthString =
                    if (selectedMonth + 1 < 10) "0${selectedMonth + 1}" else "${selectedMonth + 1}"
                // force day to be 2 digits
                val dayString = if (selectedDay < 10) "0$selectedDay" else "$selectedDay"
                val selectedDate = "$dayString/$monthString/$selectedYear"
                editText.setText(selectedDate)
            }, year, month, day)

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
        // if same day, show time, else show date and time
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

    fun getTimestampFromString(date: String, time: String): Timestamp {
        // Date format: the input format is dd/MM/yyyy HH:mm
        // Change the format to yyyy-MM-dd HH:mm:ss
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val dateTime = "$date $time"
        val localDateTime = LocalDateTime.parse(dateTime, formatter)

        // make localDateTime to String with the format yyyy-MM-dd HH:mm:ss
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = localDateTime.format(formatter2)

        // make the formattedDateTime to Timestamp
        val timestamp = Timestamp.valueOf(formattedDateTime)
        return timestamp
    }


    fun getCurrentTime(): Timestamp {
        val current = LocalDateTime.now()
        val currentTimestamp = Timestamp.valueOf(current.toString())
        return currentTimestamp
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
        return when (eventType) {
            "badminton" -> R.drawable.badminton
            "baseball" -> R.drawable.baseball
            "basketball" -> R.drawable.basketball
            "volleyball" -> R.drawable.volleyball
            "football" -> R.drawable.football
            "billiard" -> R.drawable.billiard
            "ping_pong" -> R.drawable.ping_pong
            "tennis" -> R.drawable.tennis
            "bowling" -> R.drawable.bowling
            "golf" -> R.drawable.golf
            else -> R.drawable.basketball
        }
    }
}