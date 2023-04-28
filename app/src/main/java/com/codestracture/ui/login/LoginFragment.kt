package com.codestracture.ui.login

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.codestracture.R
import com.codestracture.databinding.FragmentLoginBinding
import com.codestracture.ui.base.BaseFragment
import com.codestracture.utils.ext.checkPermissionGranted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    override val layoutId: Int = R.layout.fragment_login

    override val viewModel: LoginViewModel by viewModels()

    override fun observeEvents() {
        viewModel.viewModelEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is LoginViewModelEvent.LoginSuccess -> {
                    gotoHomeScreen()
                }
                is LoginViewModelEvent.LoginError -> {
                    showLoginErrorDialog(event.message)
                }
            }
        }
    }

    override fun initView() {
        if (!checkPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            locationPermissionsResult.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showLoginErrorDialog(message: String) {
        iDialog.showDialog(
            message = message,
            cancelable = true,
            positiveButtonId = R.string.btn_login,
            positiveClick = {
                gotoHomeScreen()
            })
    }

    private fun gotoHomeScreen() {
        LoginFragmentDirections
            .actionLoginFragmentToHomeFragment()
            .navigateTo()
    }

    private fun checkFineLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkCoarseLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private val locationPermissionsResult = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if (!it.values.contains(false)) {
            Log.d("MyTag", "Location permission Granted")
            if (checkFineLocationPermission()) {
                Log.d("MyTag", "Fine Location")
            } else if (checkCoarseLocationPermission()) {
                Log.d("MyTag", "Coarse Location")
            }
        } else {
            Log.d("MyTag", "Location permission Deny")
            if (checkFineLocationPermission()) {
                Log.d("MyTag", "Fine Location")
            } else if (checkCoarseLocationPermission()) {
                Log.d("MyTag", "Coarse Location")
                initView()
            }
        }
    }
}