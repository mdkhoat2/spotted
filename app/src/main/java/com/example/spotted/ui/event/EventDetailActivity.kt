package com.example.spotted.ui.event

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
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
import com.example.spotted.databinding.ActivityEventDetailBinding
import com.example.spotted.ui.create.CreateEventViewModel
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
import java.util.Locale

class EventDetailActivity() : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityEventDetailBinding
    private lateinit var mMap: GoogleMap

    private lateinit var description: String
    private lateinit var type: String
    private lateinit var start: String
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    // all edit text fields

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val root: View = binding.root

        setContentView(root)

        LayoutUtil.setupUI(this, root)


        val header : TextView = binding.activityEventDetailHeaderTextView
        val location  = binding.activityEventDetailLocation

        LayoutUtil.applyVariableFont(this,header,"'wght' 500, 'wdth' 150")
        LayoutUtil.applyVariableFont(this,location,"'wght' 500, 'wdth' 150")
        //LayoutUtil.applyVariableFont(this,proceed,"'wght' 500, 'wdth' 150")

        val mapFragment = supportFragmentManager.findFragmentById(R.id.activityEventDetail_mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val textName = findViewById<TextView>(R.id.text_view_content_name)
        val textType = findViewById<TextView>(R.id.text_view_content_sport)
        val textDateTime = findViewById<TextView>(R.id.text_view_content_datetime)
        val textLocation = findViewById<TextView>(R.id.text_view_content_location)

        //passing data from event to the activity
        //get the description, type, start time, latitude, and longitude
        description = intent.getStringExtra("description")?:""
        type = intent.getStringExtra("type")?:""
        start = intent.getStringExtra("start")?:""
        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)
        val address = intent.getStringExtra("address")?:""


        textName.text = description
        textType.text = type
        textDateTime.text = start


        textLocation.text = address

        LocationSetup()

        val back : ImageButton = binding.activityChangePasswordImageButtonBack
        back.setOnClickListener {
            finish()
        }

        //base on if you are the creator of the event or not, change the fragment
        // get event admin boolean
        val admin = intent.getBooleanExtra("admin", false)

    }

    private fun LocationSetup() {
        LocationHelper.initialize(this)
        LocationHelper.initializePlaces(this)


    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker and move the camera
        val location = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(location).title(description))
        // move the camera and zoom in
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }
}

