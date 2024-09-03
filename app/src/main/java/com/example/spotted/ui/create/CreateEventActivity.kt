package com.example.spotted.ui.create

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.example.spotted.MainActivity
import com.example.spotted.R
import com.example.spotted.backend.dataModels.Event
import com.example.spotted.backend.dataServices.EventDataService
import com.example.spotted.databinding.ActivityCreateEventBinding
import com.example.spotted.ui.event.MapActivity
import com.example.spotted.util.LayoutUtil
import com.example.spotted.util.LocationHelper
import com.example.spotted.util.SupportUtil
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.sql.Timestamp

class CreateEventActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityCreateEventBinding
    private lateinit var mMap: GoogleMap

    // all edit text fields
    private lateinit var nameEditText: EditText
    private lateinit var sportEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var locationPos: LatLng
    private lateinit var locationAddress: String

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

        val mapFragment = supportFragmentManager.findFragmentById(R.id.activityCreateEvent_mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        LocationSetup()

        val back : ImageButton = binding.activityChangePasswordImageButtonBack
        back.setOnClickListener {
            finish()
        }

        proceed.setOnClickListener {
//            val intent = Intent(this, MapActivity::class.java)
//            intent.putExtra("navigateTo", "home")
//            startActivity(intent)

            createTheEvent()
        }

    }

    private fun createTheEvent() {
        //check if all fields are filled and valid
        if (nameEditText.text.isEmpty() || sportEditText.text.isEmpty() || dateEditText.text.isEmpty() || timeEditText.text.isEmpty() ) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        //create the event
        val name = nameEditText.text.toString()
        val sport = sportEditText.text.toString() // is type
        val dateTime:Timestamp = SupportUtil.getTimestampFromString(dateEditText.text.toString(),timeEditText.text.toString())
        val deadline = Timestamp(System.currentTimeMillis())

        val event = Event("0", name, dateTime, 60,
            locationPos.latitude, locationPos.longitude, locationAddress,
            sport,"Everyone", 10, deadline)

        EventDataService.createEvent(event) { event ->
            if (event != null) {
                Toast.makeText(this, "Event created", Toast.LENGTH_SHORT).show()
                println("Event created: $event")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to create event", Toast.LENGTH_SHORT).show()
                println("Failed to create event")
            }
        }
    }

    private fun LocationSetup() {
        LocationHelper.initialize(this)
        LocationHelper.initializePlaces(this)
        val autocompleteFragment = supportFragmentManager.findFragmentById(R.id.activityCreateEvent_autocomplete_fragment) as AutocompleteSupportFragment

        autocompleteFragment.view?.setBackgroundResource(R.drawable.rounded_white)

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
        autocompleteFragment.setCountries("VN")
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS)

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                println("Place: ${place.name}, ${place.id}")
                locationPos = place.latLng!!
                locationAddress = place.name!!.toString()
                mMap.clear()
                mMap.addMarker(MarkerOptions().position(place.latLng!!).title(place.name))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.latLng!!, 16f))
            }

            override fun onError(status: Status) {
                println("An error occurred: $status")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupInput(view: View) {
        nameEditText = view.findViewById(R.id.activityCreateEvent_name_editText)
        sportEditText = view.findViewById(R.id.activityCreateEvent_sport_editText)

        // Date EditText
        dateEditText = view.findViewById(R.id.activityCreateEvent_date_editText)

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

        timeEditText = view.findViewById(R.id.activityCreateEvent_time_editText)

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


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker and move the camera
        LocationHelper.getCurrentLocation(this) { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.addMarker(MarkerOptions().position(currentLatLng).title("You are here"))
                val stadium = LatLng(10.762632, 106.660162)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stadium, 15f))
            }
        }
    }
}
