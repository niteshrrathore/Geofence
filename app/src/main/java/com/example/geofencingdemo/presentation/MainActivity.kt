package com.example.geofencingdemo.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.geofencingdemo.core.utils.Constants
import com.example.geofencingdemo.GeofenceApplication
import com.example.geofencingdemo.core.common.GeofenceHelper
import com.example.geofencingdemo.R
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : FragmentActivity(), OnMapReadyCallback, OnMapLongClickListener {

    private var mMap: GoogleMap? = null
    private var geofencingClient: GeofencingClient? = null
    private var geofenceHelper: GeofenceHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment !!.getMapAsync(this)

        geofencingClient = LocationServices.getGeofencingClient(this)
        geofenceHelper = GeofenceHelper(GeofenceApplication())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Move camera to default location
        mMap !!.moveCamera(CameraUpdateFactory.newLatLngZoom(Constants.AREA_LANDMARKS, 16f))

        enableUserLocation()

        mMap !!.setOnMapLongClickListener(this)
    }

    private fun enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap !!.isMyLocationEnabled = true
        } else {
            //Ask for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    Constants.FINE_LOCATION_ACCESS_REQUEST_CODE
                )
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    Constants.FINE_LOCATION_ACCESS_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                mMap !!.isMyLocationEnabled = true
            } else {
                //We don't have the permission..
            }
        }

        if (requestCode == Constants.BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                Toast.makeText(this, "You can add geofences...", Toast.LENGTH_SHORT).show()
            } else {
                //We do not have the permission..
                Toast.makeText(
                    this,
                    "Background location access is neccessary for geofences to trigger...",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onMapLongClick(latLng: LatLng) {
        if (Build.VERSION.SDK_INT >= 29) {
            //We need background permission
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                handleMapLongClick(latLng)
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        Constants.BACKGROUND_LOCATION_ACCESS_REQUEST_CODE
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        Constants.BACKGROUND_LOCATION_ACCESS_REQUEST_CODE
                    )
                }
            }
        } else {
            handleMapLongClick(latLng)
        }
    }

    private fun handleMapLongClick(latLng: LatLng) {
        mMap !!.clear()
        addMarker(latLng)
        addCircle(latLng, Constants.GEOFENCE_RADIUS_IN_METERS)
        addGeofence(latLng, Constants.GEOFENCE_RADIUS_IN_METERS)
    }

    private fun addGeofence(latLng: LatLng, radius: Float) {
        val geofence = geofenceHelper!!.getGeofence(
            Constants.GEOFENCE_ID, latLng, radius,
            Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_EXIT
        )
        val geofencingRequest = geofenceHelper!!.getGeofencingRequest(geofence)
        val pendingIntent = geofenceHelper!!.getPendingIntent(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        geofencingClient !!.addGeofences(geofencingRequest, pendingIntent)
            .addOnSuccessListener { Log.d(TAG, "onSuccess: Geofence Added...") }
            .addOnFailureListener { e ->
                val errorMessage = geofenceHelper !!.getErrorString(e)
                Log.d(TAG, "onFailure: $errorMessage")
            }
    }

    private fun addMarker(latLng: LatLng) {
        val markerOptions = MarkerOptions().position(latLng)
        mMap!!.addMarker(markerOptions)
    }

    private fun addCircle(latLng: LatLng, radius: Float) {
        val circleOptions = CircleOptions()
        circleOptions.center(latLng)
        circleOptions.radius(radius.toDouble())
        circleOptions.strokeColor(Color.argb(255, 255, 0, 0))
        circleOptions.fillColor(Color.argb(64, 255, 0, 0))
        circleOptions.strokeWidth(4f)
        mMap!!.addCircle(circleOptions)
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}