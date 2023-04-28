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

    private var mainViewClickListener: (View) -> Unit = {}

    fun setMainViewClickListener(mainViewClickListener: (View) -> Unit) {
        this.mainViewClickListener = mainViewClickListener
    }

    override fun buildModels() {
        stateList.forEachIndexed { index, s ->
            tripDetailsView {
                id(index)
                handleState(s)
                rootClickListener(this@HomeEpoxyController.mainViewClickListener)
            }
        }
    }
}