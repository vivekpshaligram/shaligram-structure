package com.codestracture.ui.main

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SmsManager
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import com.codestracture.data.local.LocalRepository
import com.codestracture.data.remote.RemoteRepository
import com.codestracture.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) : BaseViewModel() {


    fun checkSimAuth() {
        val phoneNumberToCheck = "9624582661" // Replace with the phone number to check

        val simExists = isPhoneNumberSimExists(context, phoneNumberToCheck)
        if (simExists) {
            Log.d("MyTag","simExists:: True")
            // The SIM for the given phone number exists in the device
            // Perform your desired actions here
        } else {
            Log.d("MyTag","simExists:: False")
            // The SIM for the given phone number does not exist in the device
            // Perform your desired actions here
        }
    }

    // Check if the given phone number's SIM exists in the device
    private fun isPhoneNumberSimExists(context: Context, phoneNumber: String): Boolean {
        val telephonyManager = context.getSystemService(TelephonyManager::class.java)
        if (telephonyManager != null && !TextUtils.isEmpty(phoneNumber)) {
            val subscriptionManager = context.getSystemService(SubscriptionManager::class.java)
            if (subscriptionManager != null) {
                for (subscriptionInfo in subscriptionManager.activeSubscriptionInfoList) {
                    val number = subscriptionInfo.number
                    if (!TextUtils.isEmpty(number) && (number == phoneNumber)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    // Check if the app has the necessary permissions to access SIM information
    private fun hasPermissionToReadPhoneState(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionResult = context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
            return permissionResult == PackageManager.PERMISSION_GRANTED
        }
        return true
    }

    private fun checkSimSubIdPresentOrNot(subscriptionId: Int): Boolean {
        if (hasPermissionToReadPhoneState(context)) {
            val subscriptionManager = context.getSystemService(SubscriptionManager::class.java)
            subscriptionManager.activeSubscriptionInfoList.find { it.subscriptionId == subscriptionId } != null
        }
        return false
    }

    fun sendSlientMessagePhoneVerify(mainActivity: MainActivity) {
        val payload = byteArrayOf(0x0A, 0x06, 0x03, 0xB0.toByte(), 0xAF.toByte(), 0x82.toByte(), 0x03, 0x06, 0x6A, 0x00, 0x05)
        val sentPI = PendingIntent.getBroadcast(mainActivity, 0x1337, Intent(MainActivity.SENT), PendingIntent.FLAG_UPDATE_CURRENT)
        val deliveryPI = PendingIntent.getBroadcast(mainActivity, 0x1337, Intent(MainActivity.DELIVER), PendingIntent.FLAG_UPDATE_CURRENT)

        val smsManager: SmsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mainActivity.getSystemService<SmsManager>(SmsManager::class.java)
        } else {
            SmsManager.getDefault()
        }

        smsManager.sendDataMessage("9624582661", null, 9200.toShort(), payload, sentPI, deliveryPI)
    }
}