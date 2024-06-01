package com.example.geofencingdemo.core.utils

import com.google.android.gms.maps.model.LatLng

class Constants {
    companion object {
        private const val PACKAGE_NAME = "com.google.android.gms.location.Geofence"
        const val CHANNEL_NAME = "High priority channel"
        const val CHANNEL_ID = "com.example.notifications$CHANNEL_NAME"
        const val FINE_LOCATION_ACCESS_REQUEST_CODE = 10001
        const val BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002
        const val GEOFENCE_ID = "10"
        const val GEOFENCE_NOT_AVAILABLE = "GEOFENCE_NOT_AVAILABLE"
        const val GEOFENCE_TOO_MANY_GEOFENCES = "GEOFENCE_TOO_MANY_GEOFENCES"
        const val GEOFENCE_TOO_MANY_PENDING_INTENTS = "GEOFENCE_TOO_MANY_PENDING_INTENTS"

        /**
         * Used to set an expiration time for a geofence. After this amount of time Location Services
         * stops tracking the geofence.
         */
        private const val GEOFENCE_EXPIRATION_IN_HOURS: Long = 12

        /**
         * Geofences expire after twelve hours.
         */
        const val GEOFENCE_EXPIRATION_IN_MILLISECONDS = GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000
        const val GEOFENCE_RADIUS_IN_METERS = 1609f // 1 mile, 1.6 km

        val AREA_LANDMARKS = LatLng(22.689270443004848, 75.88460299573964)

    }
}