package com.example.spotted.ui.event


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.spotted.R
import com.example.spotted.databinding.ActivityMapBinding
import com.example.spotted.util.LayoutUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
<<<<<<< Updated upstream

import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
=======
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
>>>>>>> Stashed changes
import com.google.android.libraries.places.api.net.PlacesClient


class MapActivity: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding
    private lateinit var mMap: GoogleMap
    private lateinit var searchBar: AutoCompleteTextView
<<<<<<< Updated upstream
    private lateinit var places : Places
=======
>>>>>>> Stashed changes
    private lateinit var adapter: ArrayAdapter<String>
    private val suggestions = mutableListOf<String>()
    private lateinit var placesClient: PlacesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val token = AutocompleteSessionToken.newInstance()


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

        placesClient = Places.createClient(this)


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

        val locationBias = RectangularBounds.newInstance(
            LatLng(8.1790665, 102.14441),  // Southwest corner of Vietnam
            LatLng(23.393395, 109.4696483)  // Northeast corner of Vietnam
        )

        val filteredEvents = listOf(
            "Phu Tho Stadium Thanh Hau",
            "Duy Lam Ben Thanh Market",
            "Con Cac Saigon Opera House"
        ).filter { it.contains(query, ignoreCase = true) }

        val request = FindAutocompletePredictionsRequest.builder().setQuery(query)
            .setCountries("VN")
            .setLocationBias(locationBias) // Set the location bias
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                suggestions.clear() // Clear previous suggestions

<<<<<<< Updated upstream
                for (S in filteredEvents)
                    suggestions.add(S)

                val predictions = response.autocompletePredictions
                for (prediction in predictions) {
                    if (suggestions.size>5) break
                    suggestions.add(prediction.getPrimaryText(null).toString())
                }



                val newAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, suggestions)
                searchBar.setAdapter(newAdapter)
=======
                for (S in filteredEvents) suggestions.add(S)

                val predictions = response.autocompletePredictions
                for (prediction in predictions) {
                    println(prediction)
                    println(suggestions.size)
                    if (suggestions.size>5) break
                    suggestions.add(prediction.getFullText(null).toString())
                }

                adapter.clear()
                adapter.addAll(suggestions)
                adapter.notifyDataSetChanged()

>>>>>>> Stashed changes
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker and move the camera
        val stadium = LatLng(10.762622, 106.660172) // Example coordinates
        mMap.addMarker(MarkerOptions().position(stadium).title("Badminton event in Phu Tho Stadium"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stadium, 15f))
    }
}