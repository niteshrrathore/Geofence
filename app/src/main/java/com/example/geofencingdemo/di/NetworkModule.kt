package com.example.geofencingdemo.di

import android.content.Context
import com.example.geofencingdemo.GeofenceApplication
import com.example.geofencingdemo.core.common.GeofenceHelper
import com.example.geofencingdemo.core.common.NotificationHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): GeofenceApplication {
        return app as GeofenceApplication
    }

    @Singleton
    @Provides
    fun provideGeofenceHelper(appContext: GeofenceApplication): GeofenceHelper {
        return GeofenceHelper(appContext)
    }

    @Singleton
    @Provides
    fun provideNotificationHelper(appContext: GeofenceApplication): NotificationHelper {
        return NotificationHelper(appContext)
    }

}