package com.example.spotted.ui.event

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.spotted.R
import com.example.spotted.backend.dataModels.Event
import com.example.spotted.databinding.ActivityMapBinding
import com.example.spotted.util.LayoutUtil
import com.example.spotted.util.LocationHelper
import com.example.spotted.util.SupportUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import java.sql.Timestamp


class MapActivity: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding
    private lateinit var mMap: GoogleMap
    private lateinit var searchBar: AutoCompleteTextView
    private lateinit var adapter: ArrayAdapter<String>
    private val suggestions = mutableListOf<String>()
    private lateinit var placesClient: PlacesClient

    private var eventList = mutableListOf<Event>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val token = AutocompleteSessionToken.newInstance()

        LocationHelper.initialize(this)

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
        LocationHelper.initializePlaces(this)
        placesClient = LocationHelper.getPlacesClient()

        setupAutoCompleteTextView()
    }

    private fun setupAutoCompleteTextView() {
        searchBar = findViewById(R.id.searchBar)

        // Set up the adapter with the initial custom suggestions
        adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, suggestions)
        searchBar.setAdapter(adapter)

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    performTextSearch(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        searchBar.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            searchBar.setText(selectedItem)
        }
    }

    private fun performTextSearch(query: String) {
        val filteredEvents = listOf(
            "Phu Tho Stadium Thanh Hau",
            "Duy Lam Ben Thanh Market",
            "Con Cac Saigon Opera House"
        ).filter { it.contains(query, ignoreCase = true) }

        val request = FindAutocompletePredictionsRequest.builder().setQuery(query)
            .setCountries("VN")
            .setTypeFilter(TypeFilter.ADDRESS)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                suggestions.clear() // Clear previous suggestions

                for (S in filteredEvents) suggestions.add(S)

                val predictions = response.autocompletePredictions
                for (prediction in predictions) {
                    if (suggestions.size>5) break
                    suggestions.add(prediction.getPrimaryText(null).toString())
                }

                adapter.clear();
                adapter.addAll(suggestions);
                adapter.notifyDataSetChanged();
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker and move the camera
        LocationHelper.getCurrentLocation(this) { location ->
            if (location != null) {
//                val currentLatLng = LatLng(location.latitude, location.longitude)
//                mMap.addMarker(MarkerOptions().position(currentLatLng).title("You are here"))
//                val stadium = LatLng(10.762632, 106.660162)
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stadium, 15f))
            }
        }

        eventList.clear()

        SetUpMarker()
    }

    //
    fun SetUpMarker(){ // icon for each event
        val time =  Timestamp(System.currentTimeMillis());
        //some other time for testing
        val time2 = Timestamp(System.currentTimeMillis() + 100000000)
        val time3 = Timestamp(System.currentTimeMillis() + 2000000000)
        val time4 = Timestamp(System.currentTimeMillis() + 300000000)
        val time5 = Timestamp(System.currentTimeMillis() - 4000000000)


        eventList.addAll(listOf(//random close location
            Event("1","Badminton 1",time,60, 21.022411, 105.804817,"Nhaf Hauaj","badminton","Everyone",10,time),
            Event("2","Badminton 2",time2,60,21.028511, 105.804217,"Nha Duy","baseball","Everyone",10,time),
            Event("3","Badminton 3",time3,60, 21.028531, 105.804717,"dkfjkf","basketball","Everyone",10,time),
            Event("4","Badminton 4",time4,60, 21.028501, 105.804917,"dfdfdsf","volleyball","Everyone",10,time)
        ))

        // Add all markers with icon to the map

        for (event in eventList) {
            val latLng = LatLng(event.latitude, event.longitude)

            // get the icon for the event
            val icon = SupportUtil.getSportIcon(event.type)

            // set the icon for the event
            val bitmapDescriptor = SupportUtil.bitmapDescriptorFromVector(this, icon)

            mMap.addMarker(MarkerOptions().position(latLng).title(event.type).icon(bitmapDescriptor))

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }
}
