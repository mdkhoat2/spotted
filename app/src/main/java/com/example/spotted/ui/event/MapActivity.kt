package com.example.spotted.ui.event

import android.content.Context
import android.content.Intent
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
import com.example.spotted.backend.dataModels.GetEventsRequest
import com.example.spotted.backend.dataServices.EventDataService
import com.example.spotted.databinding.ActivityMapBinding
import com.example.spotted.ui.create.CreateEventActivity
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

    private lateinit var CurrentLocation: LatLng



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

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.addButton.setOnClickListener{
            val intent = Intent(this, CreateEventActivity::class.java)
            startActivity(intent)
        }

        val sports = resources.getStringArray(R.array.sports)

        val sportAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,sports)
        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerSport.adapter=sportAdapter
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
            // Move the camera to the selected place

            val event = eventList.find { it.description == selectedItem }
            if (event != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    LatLng(event.latitude, event.longitude), 15f))
            }
            else {
                val latLng = LocationHelper.getLatLngFromAddress(this, selectedItem)
                if (latLng != LatLng(0.0, 0.0)) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }
        }
    }


    private fun performTextSearch(query: String) {
        // Filter the events based on the description and address get the description
        val filteredEvents = eventList.filter { it.description.contains(query, ignoreCase = true)
                || it.address.contains(query, ignoreCase = true) }

        val filteredName = filteredEvents.map { it.description }

        val request = FindAutocompletePredictionsRequest.builder().setQuery(query)
            .setCountries("VN")
            .setTypeFilter(TypeFilter.ESTABLISHMENT)
            .build()

//        val request = FindAutocompletePredictionsRequest.builder().setQuery(query)
//            .setCountries("VN")
//            .setTypeFilter(TypeFilter.ADDRESS)
//            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                suggestions.clear() // Clear previous suggestions

                for (S in filteredName) suggestions.add(S)

                val predictions = response.autocompletePredictions
                for (prediction in predictions) {
                    if (suggestions.size>7) break
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

        CurrentLocation = LatLng(21.028511,105.804817)

        // Add a marker and move the camera
        LocationHelper.getCurrentLocation(this) { location ->
            if (location != null) {
                //CurrentLocation = LatLng(location.latitude, location.longitude)
                CurrentLocation = LatLng(21.028511, 105.804817)
                mMap.addMarker(MarkerOptions().position(CurrentLocation).title("You are here"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CurrentLocation, 15f))
            } else
                println("Location is null")
        }

        eventList.clear()
        getEvents()
    }

    fun getEvents(){
        EventDataService.getEvents(
            GetEventsRequest(CurrentLocation.latitude, CurrentLocation.longitude,
                300000.0)) { events ->
                if (events != null) {
                    eventList.addAll(events)
                    SetUpMarker()
                }
        }
    }
    //
    fun SetUpMarker(){ // icon for each event

        for (event in eventList) {
            val latLng = LatLng(event.latitude, event.longitude)
            val icon = SupportUtil.getSportIcon(event.type)
            val bitmapDescriptor = SupportUtil.bitmapDescriptorFromVector(this, icon)

            mMap.addMarker(MarkerOptions().position(latLng).title(event.description).icon(bitmapDescriptor))
            // setup marker click listener
            mMap.setOnMarkerClickListener { marker ->
                val event = eventList.find { it.description == marker.title }
                if (event != null) {
                    // show event details
                    println("Event: ${event.description}")
                }
                true
            }
        }
    }
}
