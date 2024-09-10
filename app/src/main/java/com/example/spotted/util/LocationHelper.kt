package com.example.spotted.util

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import android.Manifest
import android.content.Context
import android.location.Geocoder
import com.example.spotted.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import java.util.Locale

object LocationHelper {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    fun initialize(activity: Activity) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    }

    fun getCurrentLocation(activity: Activity, onSuccess: (Location?) -> Unit) {
        // Check if permission is granted
        if (ActivityCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(activity,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
            ActivityCompat.requestPermissions(activity,arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),1)

            return getCurrentLocation(activity, onSuccess)
        }

        // Fetch the last known location
        val locationTask: Task<Location> = fusedLocationClient.lastLocation
        locationTask.addOnSuccessListener { location ->
            onSuccess(location)
        }
    }

    // also initialize Places API
    private lateinit var placesClient: PlacesClient

    fun initializePlaces(context: Context) {
        if (!Places.isInitialized()) {
            Places.initialize(context, context.getString(R.string.google_map_key))
            placesClient = Places.createClient(context)
        }
    }

    fun getPlacesClient(): PlacesClient {
        return placesClient
    }

    fun getLatLngFromAddress(context: Context, selectedItem: String) : LatLng {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocationName(selectedItem, 1)
        if (addresses!!.isNotEmpty()) {
            val lat = addresses[0].latitude
            val lng = addresses[0].longitude
            return LatLng(lat, lng)
        }
        return LatLng(0.0, 0.0)
    }

    fun calculateDistance(a:LatLng,b:LatLng) : Double { // in meters
        val earthRadius = 6371000.0 // in meters
        val dLat = Math.toRadians(b.latitude - a.latitude)
        val dLng = Math.toRadians(b.longitude - a.longitude)
        val x = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(a.latitude)) * Math.cos(Math.toRadians(b.latitude)) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c = 2 * Math.atan2(Math.sqrt(x), Math.sqrt(1 - x))
        return earthRadius * c

    }

}