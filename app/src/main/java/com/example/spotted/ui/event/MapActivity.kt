package com.example.spotted.ui.event

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient


class MapActivity: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding
    private lateinit var mMap: GoogleMap
    private lateinit var searchBar: AutoCompleteTextView
    private lateinit var adapter: ArrayAdapter<String>
    private val suggestions = mutableListOf<String>()
    private lateinit var placesClient: PlacesClient

    private var eventList = mutableListOf<Event>()
    private var eventStatus = mutableListOf<String>()
    private var eventListFiltered = mutableListOf<Event>()

    private var CurrentLocation: LatLng = LatLng(0.0, 0.0)
    private var DeviceLocation: LatLng = LatLng(0.0, 0.0)

    private var CurrentFilter = "All"

    private lateinit var progress: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progress = SupportUtil.createProgressDialog(this)

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

        binding.centerButton.setOnClickListener {
            val newCamPos = CameraPosition(
                DeviceLocation,15f,0f,0f
            )
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos))
        }

        val sports = resources.getStringArray(R.array.sports)
        val sportAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,sports)
        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerSport.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                CurrentFilter = sports[position]
                filterEvents()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
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

            val event = eventList.find { it.title == selectedItem }
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
        // Filter the events based on the name and address get the name
        val filteredEvents = eventList.filter { it.title.contains(query, ignoreCase = true)
                || it.address.contains(query, ignoreCase = true) }

        val filteredName = filteredEvents.map { it.title }

        val request = FindAutocompletePredictionsRequest.builder().setQuery(query)
            .setCountries("VN")
            .setTypeFilter(TypeFilter.ESTABLISHMENT)
            .build()

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
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMinZoomPreference(10f)

        LocationHelper.getCurrentLocation(this) { location ->
            if (location != null) {
                //CurrentLocation = LatLng(location.latitude, location.longitude)
                CurrentLocation = LatLng(10.8231, 106.6297)
                DeviceLocation = CurrentLocation
                binding.centerButton.callOnClick()
                getEvents()
            }
        }

        // set if user move the camera out of current range of events load more
        mMap.setOnCameraMoveListener {
            val center = mMap.cameraPosition.target
            val distance = LocationHelper.calculateDistance(center, CurrentLocation)
            if (distance > 300000.0) {
                CurrentLocation = center
                getEvents()
            }
        }
    }

    private fun getEvents(){
        eventList.clear()
        EventDataService.getEvents(
            GetEventsRequest(CurrentLocation.latitude, CurrentLocation.longitude,300000.0)) {
            events ->
                progress.dismiss()
                if (events != null) {
                    eventList.addAll(events)
                    filterEvents()
                }
        }
    }
    private fun filterEvents(){
        eventListFiltered.clear()
        eventListFiltered.addAll(eventList.filter
        { it.type == CurrentFilter || CurrentFilter == "all" })

        setUpMarker()
    }

    //
    private fun setUpMarker(){ // icon for each event
        mMap.clear()

        mMap.addMarker(MarkerOptions().position(DeviceLocation).title("You are here"))

        for (event in eventListFiltered) {
            val latLng = LatLng(event.latitude, event.longitude)
            val icon = SupportUtil.getSportIcon(event.type)
            val bitmapDescriptor = SupportUtil.bitmapDescriptorFromVector(this, icon)

            mMap.addMarker(MarkerOptions().position(latLng).title(event.title).icon(bitmapDescriptor))
            mMap.setOnMarkerClickListener { marker ->
                val event = eventListFiltered.find { it.title == marker.title }
                if (event != null) {
                    val intent = Intent(this, EventDetailActivity::class.java)

                    EventDataService.getRole(event._id){
                        if (it != null) {
                            intent.putExtra("permission", it)
                        } else {
                            intent.putExtra("permission", "none")
                        }

                        EventDataService.setCurrentEvent(event)
                        startActivity(intent)
                    }
                }
                true
            }
        }
    }
}
