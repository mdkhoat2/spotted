package com.example.spotted.ui.event

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
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

    private lateinit var event: Event

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
        val textDate = findViewById<TextView>(R.id.text_view_content_date)
        val textTime = findViewById<TextView>(R.id.text_view_content_time)
        val textLocation = findViewById<TextView>(R.id.text_view_content_location)

        val id = intent.getStringExtra("id")?:""

        EventDataService.getEventInfo(id) {
            if (it != null) {
                event = it
            }
        }

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
        val split = SupportUtil.SplitDateTimeString(start)
        textDate.text = split.first
        textTime.text = split.second

        textLocation.text = address

        LocationSetup()

        val back : ImageButton = binding.activityChangePasswordImageButtonBack
        back.setOnClickListener {
            finish()
        }


        // THE 5 DYNAMIC ELEMENTS
        val manager: TextView = binding.activityEventDetailManagerContentTextView
        manager.setOnClickListener {
            //open the manager profile - CALL API
        }

        val participants: AppCompatButton = binding.activityEventDetailViewParticipantButtonAppCompatButton
        participants.setOnClickListener {
            //open the participants list - CALL API
        }

        val request: AppCompatButton = binding.activityEventDetailRequestButtonAppCompatButton
        request.setOnClickListener {
            //send the request to join the event - CALL API
        }

        val leave: AppCompatButton = binding.activityEventDetailLeaveButtonAppCompatButton
        leave.setOnClickListener {
            //leave the event - CALL API
        }

        val adminIcon: ImageView = binding.activityEventDetailAdminImageView


        ///////////////////////////

        val managerText: TextView = binding.activityEventDetailManagerTextView
        val requestIcon: ImageView = binding.activityEventDetailRequestIconImageView
        val leaveIcon: ImageView = binding.activityEventDetailLeaveIconImageView
        val participantsIcon: ImageView = binding.activityEventDetailViewParticipantIconImageView

        //base on if you are the creator of the event or not, change the fragment
        // get event admin boolean
        val permission = intent.getStringExtra("permission")

        if (permission == "manager") {                  // THE MANAGER is an admin and can view participants
            manager.visibility = View.INVISIBLE         // cannot see the profile of yourself
            managerText.visibility = View.INVISIBLE

            request.visibility = View.INVISIBLE         // cannot request
            requestIcon.visibility = View.INVISIBLE

            leave.visibility = View.INVISIBLE           // cannot leave
            leaveIcon.visibility = View.INVISIBLE
        }
        else if (permission == "joined") {              // THE PARTICIPANTS can leave and view the manager's profile
            request.visibility = View.INVISIBLE         // cannot request again
            requestIcon.visibility = View.INVISIBLE

            participants.visibility = View.INVISIBLE    // cannot view participants
            participantsIcon.visibility = View.INVISIBLE

            adminIcon.visibility = View.INVISIBLE       // are not admin
        }
        else if (permission == "guest") {               // GUESTS can send request and view the manager's profile
            leave.visibility = View.INVISIBLE           // cannot leave
            leaveIcon.visibility = View.INVISIBLE

            participants.visibility = View.INVISIBLE    // cannot view participants
            participantsIcon.visibility = View.INVISIBLE

            adminIcon.visibility = View.INVISIBLE       // are not admin
        }
        else { // if there is an error in determining the permission, no action shall be taken but viewing only
            adminIcon.visibility = View.INVISIBLE
            manager.visibility = View.INVISIBLE
            managerText.visibility = View.INVISIBLE
            request.visibility = View.INVISIBLE
            requestIcon.visibility = View.INVISIBLE
            leave.visibility = View.INVISIBLE
            leaveIcon.visibility = View.INVISIBLE
            participants.visibility = View.INVISIBLE
            participantsIcon.visibility = View.INVISIBLE
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
        val location = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(location).title(description))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }
}

