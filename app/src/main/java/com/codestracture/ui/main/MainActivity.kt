package com.codestracture.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavDeepLink
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.codestracture.R
import com.codestracture.databinding.ActivityMainBinding
import com.codestracture.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :  BaseActivity<ActivityMainBinding, MainViewModel>()  {

    override val layoutId: Int = R.layout.activity_main

    override val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}