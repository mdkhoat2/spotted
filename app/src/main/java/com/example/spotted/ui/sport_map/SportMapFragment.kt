package com.example.spotted.ui.sport_map

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.spotted.R
import com.example.spotted.databinding.FragmentSportMapBinding
import com.example.spotted.util.LayoutUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

class SportMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentSportMapBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private lateinit var searchBar: AutoCompleteTextView

    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?
    ): View {
        val sportMapViewModel = ViewModelProvider(this).get(SportMapViewModel::class.java)

        _binding = FragmentSportMapBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // Initialize the map
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        LayoutUtil.setupUI(requireActivity(), root )

        setUpPlaces()

        val backBtn: ImageButton = root.findViewById(R.id.backButton)
        backBtn.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }

        val SportSpinner: Spinner = root.findViewById(R.id.spinnerSport)

        val sports = resources.getStringArray(R.array.sports)

        val sportAdapter = ArrayAdapter(requireActivity(),android.R.layout.simple_spinner_item,sports)
        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        SportSpinner.adapter=sportAdapter

        return root
    }

    private fun setUpPlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), getString(R.string.google_map_key))
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker and move the camera
        val stadium = LatLng(10.762622, 106.660172) // Example coordinates
        mMap.addMarker(MarkerOptions().position(stadium).title("Badminton event in Phu Tho Stadium"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stadium, 15f))
    }
}