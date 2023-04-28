package com.codestracture.ui.home

import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.codestracture.ui.view.tripDetailsView

class HomeEpoxyController : EpoxyController() {

    private val stateList: ArrayList<String> = ArrayList()
    fun setListStateData(stateList: List<String>) {
        this.stateList.addAll(stateList)
        requestModelBuild()
    }

    private var rootViewClickListener: (View) -> Unit = {}

    fun setRootViewClickListener(rootViewClickListener: (View) -> Unit) {
       this.rootViewClickListener = rootViewClickListener
    }

    override fun buildModels() {
        stateList.forEachIndexed { index, s ->
            tripDetailsView {
                id(index)
                handleState(s)
                rootClickListener(this@HomeEpoxyController.rootViewClickListener)
            }
        }
    }
}