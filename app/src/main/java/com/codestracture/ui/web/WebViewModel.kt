package com.codestracture.ui.web

import com.codestracture.data.local.LocalRepository
import com.codestracture.data.manager.location.LocationManager
import com.codestracture.data.remote.RemoteRepository
import com.codestracture.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    private val locationManager: LocationManager
) : BaseViewModel()