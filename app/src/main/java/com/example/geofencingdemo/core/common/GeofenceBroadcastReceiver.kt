package com.example.geofencingdemo.core.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.geofencingdemo.R
import com.example.geofencingdemo.presentation.MainActivity
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import javax.inject.Inject

class GeofenceBroadcastReceiver: BroadcastReceiver() {

    companion object {
        private const val TAG = "GeofenceBroadcastReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notificationHelper = NotificationHelper(context)
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent?.hasError() == true) {
            Log.d(TAG, "onReceive: hasError receiving..")
            return
        }

        //Location location = geofencingEvent.getTriggeringLocation();
        val transitionType = geofencingEvent?.geofenceTransition

        when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                notificationHelper.sendHighPriorityNotification(
                    context.getString(R.string.app_name), "GEOFENCE_TRANSITION_ENTER",
                    MainActivity::class.java
                )
            }

            Geofence.GEOFENCE_TRANSITION_DWELL -> {
                notificationHelper.sendHighPriorityNotification(
                    context.getString(R.string.app_name), "GEOFENCE_TRANSITION_DWELL",
                    MainActivity::class.java
                )
            }

            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                notificationHelper.sendHighPriorityNotification(
                    context.getString(R.string.app_name), "GEOFENCE_TRANSITION_EXIT",
                    MainActivity::class.java
                )
            }
        }
    }

}