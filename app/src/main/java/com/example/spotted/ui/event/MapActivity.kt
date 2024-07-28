package com.example.spotted.ui.event


import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.VolleyLog.TAG
import com.example.spotted.R
import com.example.spotted.backend.AuthDataService
import com.example.spotted.databinding.ActivityMapBinding
import com.example.spotted.util.LayoutUtil
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class MapActivity: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding
    private var authDataService = AuthDataService()
    private lateinit var mMap: GoogleMap
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private lateinit var searchBar: AutoCompleteTextView
    private lateinit var places : Places

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)


        LayoutUtil.setupUI(this, binding.root )

        setUpPlaces()

        val backBtn: ImageButton = findViewById(R.id.backButton)
        backBtn.setOnClickListener {
            finish()
        }

        val SportSpinner: Spinner = findViewById(R.id.spinnerSport)

        val sports = resources.getStringArray(R.array.sports)

        val sportAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,sports)
        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        SportSpinner.adapter=sportAdapter
    }

    private fun setUpPlaces() {
        // Initialize the SDK
        val apiKey = "AIzaSyAN5IUqcZbfL66xvabQiySJGlx2ol-6QSE"

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        val autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // Get info about the selected place.
                Log.i(TAG, "Place: ${place.name}, ${place.id}")
            }

            override fun onError(status: Status) {
                // Handle the error.
                Log.i(TAG, "An error occurred: $status")
            }
        })
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker and move the camera
        val stadium = LatLng(10.762622, 106.660172) // Example coordinates
        mMap.addMarker(MarkerOptions().position(stadium).title("Badminton event in Phu Tho Stadium"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stadium, 15f))
    }
}
