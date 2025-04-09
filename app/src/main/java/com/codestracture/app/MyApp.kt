package com.codestracture.app

import com.codestracture.utils.EpubUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : App() {
    override fun onCreate() {
        super.onCreate()
        EpubUtils.initFolders(this)
    }
}
