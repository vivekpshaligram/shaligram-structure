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
import com.codestracture.utils.ext.checkPermissionGranted
import com.codestracture.utils.ext.requestPermissions
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min
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
                is HomeViewModelEvent.Test -> {
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

        if (checkPermissionGranted(Manifest.permission.CAMERA).not()) {
            requestPermissions(permissionsRequest, arrayOf(Manifest.permission.CAMERA))
        }

        binding.epoxyRecyclerView.setController(homeController)

        homeController.setRootViewClickListener {
            Log.d("MyTag", "RootViewClick")
        }

        val list = ArrayList<String>()
        for (i in 1..105) {
            list.add("Item$i")
        }

        Log.d("MyTag", "ListSize:${list.size}")
        Log.d("MyTag", "LastItem:${list.lastOrNull()}")

        var currentPage = 1

        binding.stopUpdate.setOnClickListener {
            // viewModel.stopLocationUpdate()
            /*val intent = Intent(requireContext(), LauncherActivity::class.java)
            intent.data = Uri.parse("https://beer.conn.dev?client_version=1")
            startActivity(intent)*/
            Log.d("MyTag", "currentPage:$currentPage")
            val newList = getPage(list, currentPage, 10)
            Log.d("MyTag", "newListSize:${newList.size}")
            Log.d("MyTag", "newListLastData:${newList.lastOrNull()}")
            currentPage += 1
        }
    }

    fun <T> getPage(sourceList: List<T>?, page: Int, pageSize: Int): List<T> {
        // require(!(pageSize <= 0 || page <= 0)) { "invalid page size: $pageSize" }
        val fromIndex = (page - 1) * pageSize
        return if (sourceList == null || sourceList.size <= fromIndex) {
            Collections.emptyList()
        } else sourceList.subList(fromIndex, min(fromIndex + pageSize, sourceList.size))
    }
}