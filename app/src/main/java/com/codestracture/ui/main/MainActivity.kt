package com.codestracture.ui.main

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.codestracture.R
import com.codestracture.databinding.ActivityMainBinding
import com.codestracture.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    companion object {
        const val SENT = "pingsms.sent"
        const val DELIVER = "pingsms.deliver"
    }

    override val layoutId: Int = R.layout.activity_main

    override val viewModel: MainViewModel by viewModels()

    private val sentFilter = IntentFilter(SENT)
    private val deliveryFilter  = IntentFilter(DELIVER)

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.checkSimAuth()

        binding.rootViewLayout.setOnClickListener {
            if (binding.bottomSheet.isVisible) {
                slideToBottom(binding.bottomSheet)
            }
        }

        binding.btnShowBottomSheetDialog.setOnClickListener {
            viewModel.sendSlientMessagePhoneVerify(this)
           /* if (binding.bottomSheet.isVisible) {
                slideToBottom(binding.bottomSheet)
            } else {
                slideToTop(binding.bottomSheet)
            }*/
        }
    }

    fun getCurrentFragment() = binding.myNavHostFragment.getFragment<Fragment>()

    override fun onResume() {
        super.onResume()
        registerReceiver(br, sentFilter)
        registerReceiver(br, deliveryFilter)
        //registerReceiver(br, wapDeliveryFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(br)
    }

    private fun slideToBottom(view: View) {
        val animate = TranslateAnimation(0f, 0f, 0f, view.height.toFloat() + binding.bottomSheet.height)
        animate.duration = 500
        animate.fillAfter = true
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                view.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        view.startAnimation(animate)
    }

    private fun slideToTop(view: View) {
        view.visibility = View.INVISIBLE
        view.post {
            val animate = TranslateAnimation(0f, 0f, view.height.toFloat() + binding.bottomSheet.height, 0f)
            animate.duration = 500
            animate.fillAfter = true
            view.startAnimation(animate)
            view.visibility = View.VISIBLE
        }
    }

    fun View.visible() {
        this.visibility = View.VISIBLE
    }

    fun View.gone() {
        this.visibility = View.GONE
    }

    val br = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.e(
                "MyTag",
                "intent: " + if (intent == null || intent.action == null) "null" else intent.action
            )
        }
    }
}