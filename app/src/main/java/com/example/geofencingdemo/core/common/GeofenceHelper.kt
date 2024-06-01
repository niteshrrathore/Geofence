package com.example.geofencingdemo.core.common

import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import com.example.geofencingdemo.core.utils.Constants
import com.example.geofencingdemo.GeofenceApplication
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject


class GeofenceHelper(base: Context) : ContextWrapper(base) {

    fun getGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder()
            .addGeofence(geofence)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .build()
    }

    fun getGeofence(id: String, latLng: LatLng, radius: Float, transitionTypes: Int): Geofence {
        return Geofence.Builder()
            .setCircularRegion(latLng.latitude, latLng.longitude, radius)
            .setRequestId(id)
            .setTransitionTypes(transitionTypes)
            .setLoiteringDelay(5000)
            .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS/*Geofence.NEVER_EXPIRE*/)
            .build()
    }

    fun getPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(context, 2607,
            intent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun getErrorString(e: Exception): String? {
        if (e is ApiException) {
            when (e.statusCode) {
                GeofenceStatusCodes
                    .GEOFENCE_NOT_AVAILABLE -> return Constants.GEOFENCE_NOT_AVAILABLE

                GeofenceStatusCodes
                    .GEOFENCE_TOO_MANY_GEOFENCES -> return Constants.GEOFENCE_TOO_MANY_GEOFENCES

                GeofenceStatusCodes
                    .GEOFENCE_TOO_MANY_PENDING_INTENTS -> return Constants.GEOFENCE_TOO_MANY_PENDING_INTENTS
            }
        }
        return e.localizedMessage
    }

}