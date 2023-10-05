package com.codestracture.ui.login

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.codestracture.R
import com.codestracture.data.api.model.request.LoginReqData
import com.codestracture.data.api.model.request.Order
import com.codestracture.databinding.FragmentLoginBinding
import com.codestracture.ui.base.BaseFragment
import com.codestracture.utils.ext.checkPermissionGranted
import com.squareup.moshi.Json
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Field
import java.util.Arrays

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

        val data = LoginReqData("12364","vivek@test.com")
        val fields: Array<Field> = data.javaClass.declaredFields
        val methods = data.javaClass.declaredMethods

        Log.d("MyTag","Before Filter")
        fields.forEach {
            Log.d("MyTag","Field Name:${it.name}")
        }

        methods.forEach {
            Log.d("MyTag","Method Name:${it.name}")
        }

        Log.d("MyTag","After Filter")
        fieldSortedByOrder(fields)

        fields.forEach {
            Log.d("MyTag","Field Name:${it.name}")
            val field: Field = data.javaClass.getDeclaredField(it.name)
            field.isAccessible = true
            val value = field[data]
            Log.d("MyTag","Field Value:${value}")

            val json: Json? = field.getAnnotation(Json::class.java)
            Log.d("MyTag","Json::${json?.ignore}")
        }

    }

    private fun fieldSortedByOrder(fields: Array<Field>) {
        Arrays.sort(fields) { field1: Field, field2: Field ->
            val ob1: Order? = field1.getAnnotation(Order::class.java)
            val ob2: Order? = field2.getAnnotation(Order::class.java)
            if (ob1 != null && ob2 != null) {
                return@sort ob1.value - ob2.value
            } else {
                return@sort -1
            }
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