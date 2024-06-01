package com.example.geofencingdemo.core.common

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.geofencingdemo.core.utils.Constants
import com.example.geofencingdemo.GeofenceApplication
import com.example.geofencingdemo.R
import java.util.Random
import javax.inject.Inject

class NotificationHelper(context: Context) : ContextWrapper(context) {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createChannels() {
        val notificationChannel =
            NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.enableVibration(true)
        notificationChannel.description = "Description of the channel."
        notificationChannel.lightColor = Color.RED
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)
    }

    fun sendHighPriorityNotification(title: String?, body: String?, activityName: Class<*>?) {
        /*val intent = Intent(this, activityName)
        val pendingIntent =
            PendingIntent.getActivity(this, 267, intent, PendingIntent.FLAG_UPDATE_CURRENT)*/

        val notification =
            NotificationCompat.Builder(this, Constants.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.gefence_img)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(
                    NotificationCompat.BigTextStyle().setSummaryText("summary")
                        .setBigContentTitle(title).bigText(body)
                )
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.gefence_img))
                .setAutoCancel(true)
                .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
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
        NotificationManagerCompat.from(this).notify(Random().nextInt(), notification)
    }

}