package com.codestracture.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codestracture.data.local.LocalRepository
import com.codestracture.data.remote.RemoteRepository
import com.codestracture.ui.base.BaseViewModel
import com.codestracture.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) : BaseViewModel() {

    val viewModelEvent: SingleLiveEvent<HomeViewModelEvent> = SingleLiveEvent()

    private val mutableLiveData: MutableLiveData<List<String>> = MutableLiveData()

    init {
        fetchNewTripDetails()
    }

    fun getLiveData(): LiveData<List<String>> = mutableLiveData

    private fun fetchNewTripDetails() {
        viewModelScope.launch(MAIN) {
            localRepository.getTripsData()
                .catch {
                    Log.d("MyTag", "Catch: ${it.message}")
                }
                .collect { list ->
                    mutableLiveData.postValue(list)
                }
        }
    }
}