package com.example.spotted.ui.event

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.spotted.ParticipantActivity
import com.example.spotted.R
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.backend.dataServices.DataService
import com.example.spotted.backend.dataServices.EventDataService
import com.example.spotted.databinding.ActivityEventDetailBinding
import com.example.spotted.ui.account.Helper
import com.example.spotted.ui.profile.ProfileActivity
import com.example.spotted.util.LayoutUtil
import com.example.spotted.util.LocationHelper
import com.example.spotted.util.SupportUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class EventDetailActivity() : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityEventDetailBinding
    private lateinit var mMap: GoogleMap

    private lateinit var name: String
    private lateinit var description: String
    private lateinit var type: String
    private lateinit var start: String
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private lateinit var progress: ProgressDialog

    // all edit text fields

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val root: View = binding.root

        setContentView(root)
        LayoutUtil.setupUI(this, root)

        progress = SupportUtil.createProgressDialog(this)

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
        val textDescription = findViewById<TextView>(R.id.text_view_content_description)

        //passing data from event to the activity
        //get the description, type, start time, latitude, and longitude
        name = EventDataService.getCurrentEvent()!!.title?:""
        description = EventDataService.getCurrentEvent()!!.description?:""
        type = EventDataService.getCurrentEvent()!!.type
        start = SupportUtil.translateTime(EventDataService.getCurrentEvent()!!.start)
        latitude = EventDataService.getCurrentEvent()!!.latitude
        longitude = EventDataService.getCurrentEvent()!!.longitude
        val address = EventDataService.getCurrentEvent()!!.address
        val split = SupportUtil.SplitDateTimeString(start)

        textName.text = name
        textType.text = type
        textDate.text = split.first
        textTime.text = split.second
        textLocation.text = address
        textDescription.text = description

        LocationSetup()

        val back : ImageButton = binding.activityChangePasswordImageButtonBack
        back.setOnClickListener {
            finish()
        }

        // THE 5 DYNAMIC ELEMENTS
        val manager: TextView = binding.activityEventDetailManagerContentTextView

        //set text for the manager
        EventDataService.getAdmins(EventDataService.getCurrentEvent()!!._id) { it ->
            if (it != null) {
                AuthDataService.getUser(it[0].userID) {itt->
                    if (itt != null) {
                        manager.text = itt.name
                    }
                }

                manager.setOnClickListener {itt->
                    if (itt != null) {
                         val intent = Intent(this, ProfileActivity::class.java)
                         intent.putExtra("otherId", it[0].userID)
                         intent.putExtra("isNeedSent", true)
                         startActivity(intent)
                    }
                }
            }

        }



        val participants: AppCompatButton = binding.activityEventDetailViewParticipantButtonAppCompatButton
        participants.setOnClickListener {
            intent = Intent(this, ParticipantActivity::class.java)
            startActivity(intent)
        }

        val request: AppCompatButton = binding.activityEventDetailRequestButtonAppCompatButton
        request.setOnClickListener {
            request.isClickable = false
            val dialog = SupportUtil.createProgressDialog(this)
            EventDataService.requestJoinEvent(EventDataService.getCurrentEvent()!!._id,
                DataService.getAuthProfile()?._id ?: ""
            ) {
                dialog.dismiss()
                if (it != null) {
                    SupportUtil.createSnackBar(binding.root, "Request sent"){
                        finish()
                    }
                } else {
                    Helper.createDialog(this, "Failed", DataService.getMsg()){}
                }
            }
        }


        val adminIcon: ImageView = binding.activityEventDetailAdminImageView

        ///////////////////////////

        val managerText: TextView = binding.activityEventDetailManagerTextView
        val requestIcon: ImageView = binding.activityEventDetailRequestIconImageView
        val participantsIcon: ImageView = binding.activityEventDetailViewParticipantIconImageView

        //base on if you are the creator of the event or not, change the fragment
        // get event admin boolean
        val permission = intent.getStringExtra("permission")

        if (permission == "admin") {                  // THE MANAGER is an admin and can view participants
            manager.visibility = View.INVISIBLE         // cannot see the profile of yourself
            managerText.visibility = View.INVISIBLE

            request.visibility = View.INVISIBLE         // cannot request
            requestIcon.visibility = View.INVISIBLE
        }
        else if (permission == "participant") {              // THE PARTICIPANTS can leave and view the manager's profile
            request.visibility = View.INVISIBLE         // cannot request again
            requestIcon.visibility = View.INVISIBLE

            participants.visibility = View.INVISIBLE    // cannot view participants
            participantsIcon.visibility = View.INVISIBLE

            adminIcon.visibility = View.INVISIBLE       // are not admin
        }
        else if (permission == "none") {               // GUESTS can send request and view the manager's profile

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
            participants.visibility = View.INVISIBLE
            participantsIcon.visibility = View.INVISIBLE
        }


        LayoutUtil.setupUI(this,binding.root)
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
        progress.dismiss()
    }
}

