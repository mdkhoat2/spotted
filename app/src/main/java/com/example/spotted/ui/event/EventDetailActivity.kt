package com.example.spotted.ui.event

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

class EventDetailActivity(event: Event) : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityCreateEventBinding
    private lateinit var mMap: GoogleMap

    // all edit text fields

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val createEventViewModel = ViewModelProvider(this).get(CreateEventViewModel::class.java)

        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val root: View = binding.root

        setContentView(root)

        LayoutUtil.setupUI(this, root)


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

