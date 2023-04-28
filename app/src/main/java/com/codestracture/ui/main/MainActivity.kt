package com.codestracture.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.codestracture.R
import com.codestracture.databinding.ActivityMainBinding
import com.codestracture.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutId: Int = R.layout.activity_main

    override val viewModel: MainViewModel by viewModels()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rootViewLayout.setOnClickListener {
            if (binding.bottomSheet.isVisible) {
                slideToBottom(binding.bottomSheet)
            }
        }

        binding.btnShowBottomSheetDialog.setOnClickListener {
            if (binding.bottomSheet.isVisible) {
                slideToBottom(binding.bottomSheet)
            } else {
                slideToTop(binding.bottomSheet)
            }
        }
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
}