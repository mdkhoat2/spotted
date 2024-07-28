package com.example.spotted.ui.event


import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.spotted.R
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.databinding.ActivityMapBinding
import com.example.spotted.util.LayoutUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places

class MapActivity: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding
    private lateinit var mMap: GoogleMap
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private lateinit var searchBar: AutoCompleteTextView

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
        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_map_key))
        }

//        val autocompleteFragment = childFragmentManager
//            .findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
//
//        // Specify the types of place data to return.
//        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
//
//        // Set up a PlaceSelectionListener to handle the response.
//        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
//            override fun onPlaceSelected(place: Place) {
//                // TODO: Get info about the selected place.
//                val latLng = place.latLng
//                if (latLng != null) {
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
//                    mMap.addMarker(MarkerOptions().position(latLng).title(place.name))
//                }
//            }
//
//            override fun onError(status: com.google.android.gms.common.api.Status) {
//            }
//        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker and move the camera
        val stadium = LatLng(10.762622, 106.660172) // Example coordinates
        mMap.addMarker(MarkerOptions().position(stadium).title("Badminton event in Phu Tho Stadium"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stadium, 15f))
    }
}
