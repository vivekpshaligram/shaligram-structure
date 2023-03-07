package com.codestracture.ui.home

import android.Manifest
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.codestracture.R
import com.codestracture.databinding.FragmentHomeBinding
import com.codestracture.ui.base.BaseFragment
import com.codestracture.utils.ext.checkAllPermissionGranted
import com.codestracture.utils.ext.requestPermissions
import dagger.hilt.android.AndroidEntryPoint
import com.codestracture.ui.home.HomeEpoxyController as HomeController

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val layoutId: Int = R.layout.fragment_home

    override val viewModel: HomeViewModel by viewModels()

    private lateinit var permissionsRequest: ActivityResultLauncher<Array<String>>

    private val homeController: HomeController by lazy { HomeController() }

    override fun observeEvents() {
        viewModel.viewModelEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is HomeViewModelEvent.TripDetailsSuccess -> {
                    homeController.setListStateData(event.data)
                }
            }
        }

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            homeController.setListStateData(it)
        }
    }

    override fun initView() {
        permissionsRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                Log.d("MyTag", "Permission:" + it.values.toString())
                if (it.checkAllPermissionGranted()) {
                    Log.d("MyTag", "All Permission Granted")
                } else {
                    iDialog.showToast("Permission Not Granted")
                }
            }

        requestPermissions(permissionsRequest, arrayOf(Manifest.permission.CAMERA))

        binding.epoxyRecyclerView.setController(homeController)

        homeController.setRootClickListener {
            Log.d("MyTag","RootViewClick")
        }
    }
}