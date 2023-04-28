package com.codestracture.data.manager.location

import android.location.Location
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface LocationManager {
    fun getCurrentLocation(): Flow<Location?>
    fun getStartLocationUpdates(externalScope: CoroutineScope): Flow<Location>
}