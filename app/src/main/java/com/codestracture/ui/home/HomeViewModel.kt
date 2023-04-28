package com.codestracture.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codestracture.data.local.LocalRepository
import com.codestracture.data.manager.location.LocationManager
import com.codestracture.data.remote.RemoteRepository
import com.codestracture.ui.base.BaseViewModel
import com.codestracture.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    private val locationManager: LocationManager
) : BaseViewModel() {

    val viewModelEvent: SingleLiveEvent<HomeViewModelEvent> = SingleLiveEvent()

    private val mutableLiveData: MutableLiveData<List<String>> = MutableLiveData()

    private var locationFlow: Job? = null

    init {
        fetchNewTripDetails()
        onCurrentLocation()
        // startLocationUpdates()
    }

    private fun onCurrentLocation() {
        locationManager.getCurrentLocation()
            .flowOn(IO)
            .catch {
                Log.d("MyTag", "Error:${it.message}")
            }.onEach { location ->
                Log.d("MyTag", "Location:: $location")
            }.launchIn(viewModelScope)
    }

    private fun startLocationUpdates() {
        locationFlow = locationManager.getStartLocationUpdates(viewModelScope)
            .catch {
                Log.d("MyTag", "Location Catch: ${it.message}")
            }
            .onEach { location ->
                Log.d("MyTag", "Location: $location")
            }
            .launchIn(viewModelScope)
    }

    fun stopLocationUpdate() {
        locationFlow?.cancel()
    }

    fun getLiveData(): LiveData<List<String>> = mutableLiveData

    private fun fetchNewTripDetails() {
        viewModelScope.launch(MAIN) {
            localRepository.getTripsData()
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.d("MyTag", "Catch: ${it.message}")
                }
                .collect { list ->
                    mutableLiveData.postValue(list)
                }
        }
    }
}