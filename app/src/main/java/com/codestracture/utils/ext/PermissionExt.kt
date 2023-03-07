package com.codestracture.utils.ext

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.requestPermissions(
    request: ActivityResultLauncher<Array<String>>,
    permissions: Array<String>
) = request.launch(permissions)

fun Fragment.isAllPermissionsGranted(permissions: Array<String>) = permissions.all {
    ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
}

fun Map<String, Boolean>.checkAllPermissionGranted() = this.containsValue(false).not()