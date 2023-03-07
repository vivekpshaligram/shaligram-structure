package com.codestracture.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers

abstract class BaseViewModel : ViewModel() {

    val MAIN = Dispatchers.Main
    val IO = Dispatchers.IO

}