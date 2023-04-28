package com.codestracture.data.manager.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import com.codestracture.utils.ext.checkPermissionGranted
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class LocationManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : com.codestracture.data.manager.location.LocationManager {

    private val locationManager: LocationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Flow<Location?> = flow {
        if (context.checkPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null)
                emit(location)
            else
                throw Throwable("Location Service Not Enable")
        } else {
            throw Throwable("Location Permission Not Granted")
        }
    }

    @SuppressLint("MissingPermission")
    override fun getStartLocationUpdates(externalScope: CoroutineScope): Flow<Location> =
        callbackFlow {
            val locationListener = LocationListener { location ->
                trySend(location)
            }

            if (context.checkPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000L,
                    10F,
                    locationListener,
                    Looper.getMainLooper()
                )
            } else {
                close()
            }

            awaitClose {
                locationManager.removeUpdates(locationListener)
            }
        }.shareIn(
            externalScope,
            replay = 0,
            started = SharingStarted.WhileSubscribed()
        )
}